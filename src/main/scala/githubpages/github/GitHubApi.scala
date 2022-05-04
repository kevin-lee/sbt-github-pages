package githubpages.github

import cats._
import cats.data.{EitherT, NonEmptyVector}
import cats.effect.{Concurrent, Temporal}
import cats.syntax.all._
import effectie.cats.EitherTSupport._
import effectie.cats.{CanCatch, Fx}
import filef.FileF
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.StaticAccessToken
import github4s.{GHError, GHResponse, Github, GithubConfig}
import githubpages.github.Data.{CommitInfo, GitHubApiConfig}
import loggerf.cats._
import loggerf.syntax._
import org.http4s.client.Client

import java.io.File
import java.util.Base64
import scala.concurrent.duration._

/** @author Kevin Lee
  * @since 2020-05-27
  */
object GitHubApi {

  val base64: Base64.Encoder         = Base64.getEncoder
  val base64Encoding: Option[String] = "base64".some

  val essentialHeaders: Map[String, String] = Map("user-agent" -> "sbt-github-pages")

  val blobMode: String = "100644"
  val blobType: String = "blob"

  val defaultTextExtensions: Set[String] = Set(".md", ".css", ".html", ".xml", ".js", ".txt")
  val defaultMaximumLength: Int          = 10240

  val defaultBlobConfig: Data.BlobConfig = Data.BlobConfig(defaultTextExtensions, defaultMaximumLength)

  def buildIsText(blobConfig: Data.BlobConfig): Data.IsText = Data.IsText(blobConfig)

  /*
   * https://docs.github.com/en/rest/overview/resources-in-the-rest-api#abuse-rate-limits
   */
  private def githubWithAbuseRateLimit[F[_]: Temporal](github: Github[F]): F[Github[F]] =
    Temporal[F].sleep(1.second).as(github)

  private def fetchHeadCommit[F[_]: Fx: Monad](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    branch: Data.Branch,
    headers: Map[String, String],
  ): EitherT[F, GitHubError, Ref] =
    EitherT(
      processResponse(
        github
          .gitData
          .getReference(
            gitHubRepo.org.org,
            gitHubRepo.repo.repo,
            s"heads/${branch.branch}",
            pagination = none,
            headers,
          )
      )
    ).subflatMap { refs =>
      refs
        .find(_.ref === s"refs/heads/${branch.branch}")
        .toRight(GitHubError.branchNotFound(gitHubRepo, branch))
    }

  private def updateHead[F[_]: Fx: Monad](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    branch: Data.Branch,
    commitSha: Data.CommitSha,
    headers: Map[String, String],
  ): EitherT[F, GitHubError, Ref] =
    EitherT(
      processResponse(
        github
          .gitData
          .updateReference(
            gitHubRepo.org.org,
            gitHubRepo.repo.repo,
            s"heads/${branch.branch}",
            commitSha.commitSha,
            force = false,
            headers,
          )
      )
    )

  private def processResponse[F[_]: Fx: Functor, A](
    response: F[GHResponse[A]]
  ): F[Either[GitHubError, A]] =
    response.map(
      _.result.leftMap(err => GitHubError.requestFailure(err))
    )

  private def findBaseTreeCommit[F[_]: Fx: Monad](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    commitSha: Option[Data.CommitSha],
    headers: Map[String, String],
  ): EitherT[F, GitHubError, Option[RefCommit]] =
    commitSha.flatTraverse(sha =>
      EitherT(
        processResponse(
          github.gitData.getCommit(gitHubRepo.org.org, gitHubRepo.repo.repo, sha.commitSha, headers)
        )
      ).map(Option(_))
    )

  private def createTreeDataList[F[_]: Fx: CanCatch: Monad: Temporal](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    baseDir: Data.BaseDir,
    files: List[File],
    isText: Data.IsText,
    headers: Map[String, String],
  ): EitherT[F, GitHubError, List[TreeData]] = {

    def treeDataWithBase64Encoding(
      github: Github[F],
      gitHubRepo: Data.GitHubRepo,
      filePath: String,
      bytes: Array[Byte],
      headers: Map[String, String],
    ): EitherT[F, GitHubError, TreeData] =
      for {
        githubWithRateLimit <- eitherTRightF(githubWithAbuseRateLimit(github))
        response            <- EitherT(
                                 processResponse(
                                   githubWithRateLimit
                                     .gitData
                                     .createBlob(
                                       gitHubRepo.org.org,
                                       gitHubRepo.repo.repo,
                                       base64.encodeToString(bytes),
                                       base64Encoding,
                                       headers,
                                     )
                                 )
                               )
                                 .map(refInfo => TreeDataSha(filePath, blobMode, blobType, refInfo.sha): TreeData)
      } yield response

    def createTreeData(
      github: Github[F],
      gitHubRepo: Data.GitHubRepo,
      file: File,
      filePath: String,
      bytes: Array[Byte],
      isText: Data.IsText,
      headers: Map[String, String],
    ): EitherT[F, GitHubError, TreeData] =
      if (isText(file, bytes))
        eitherTRightPure[GitHubError](
          TreeDataBlob(filePath, blobMode, blobType, new String(bytes))
        )
      else
        treeDataWithBase64Encoding(github, gitHubRepo, filePath, bytes, headers)

    def processFile(
      github: Github[F],
      gitHubRepo: Data.GitHubRepo,
      baseDir: Data.BaseDir,
      file: File,
      isText: Data.IsText,
      headers: Map[String, String],
    ): EitherT[F, GitHubError, TreeData] =
      for {
        filePath <- EitherT(FileF.relativePathOf(baseDir.baseDir, file))
                      .leftMap(
                        GitHubError.fileHandling(
                          s"getting relative path to process {file: $file} with {baseDir: ${baseDir.baseDir}}"
                        )
                      )
        bytes    <- EitherT(FileF.readBytesFromFile(file, FileF.BufferSize(2048)))
                      .leftMap(
                        GitHubError.fileHandling(
                          s"reading bytes from [file: $file] with BufferSize(2048)"
                        )
                      )
        treeData <- createTreeData(github, gitHubRepo, file, filePath, bytes, isText, headers)
      } yield treeData

    files.traverse(file => processFile(github, gitHubRepo, baseDir, file, isText, headers))
  }

  private def createTree[F[_]: Fx: Temporal: Monad](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    baseTreeSha: Option[String],
    treeData: List[TreeData],
    headers: Map[String, String],
  ): EitherT[F, GitHubError, TreeResult] =
    for {
      githubWithRateLimit <- eitherTRightF(githubWithAbuseRateLimit(github))
      response            <- EitherT(
                               processResponse(
                                 githubWithRateLimit
                                   .gitData
                                   .createTree(
                                     gitHubRepo.org.org,
                                     gitHubRepo.repo.repo,
                                     baseTreeSha,
                                     treeData,
                                     headers,
                                   )
                               )
                             )
    } yield response

  private def createCommit[F[_]: Fx: Monad: Temporal: Monad](
    github: Github[F],
    gitHubRepo: Data.GitHubRepo,
    commitMessage: Data.CommitMessage,
    treeResultSha: Data.TreeResultSha,
    parentCommitSha: Data.CommitSha,
    headers: Map[String, String],
  ): EitherT[F, GitHubError, RefCommit] =
    for {
      githubWithRateLimit <- eitherTRightF(githubWithAbuseRateLimit(github))
      response            <- EitherT(
                               processResponse(
                                 githubWithRateLimit
                                   .gitData
                                   .createCommit(
                                     gitHubRepo.org.org,
                                     gitHubRepo.repo.repo,
                                     commitMessage.commitMessage,
                                     treeResultSha.treeResultSha,
                                     List(parentCommitSha.commitSha),
                                     author = none,
                                     headers,
                                   )
                               )
                             )
    } yield response

  private def updateCommitFiles[F[_]: Fx: CanCatch: Monad: Log: Temporal](
    github: Github[F],
    commitInfo: CommitInfo,
    baseDir: Data.BaseDir,
    allFiles: Vector[File],
    isText: Data.IsText,
    commitSha: Option[Data.CommitSha],
    headers: Map[String, String],
  ): EitherT[F, GitHubError, Option[Data.CommitSha]] =
    if (allFiles.isEmpty) {
      eitherTRight[GitHubError](commitSha)
    } else {
      for {
        parentCommitSha     <-
          commitSha.fold(
            fetchHeadCommit(github, commitInfo.gitHubRepo, commitInfo.branch, headers)
              .map(ref => Data.CommitSha(ref.`object`.sha))
          )(sha => eitherTRightPure[GitHubError](sha))
        maybeBaseTreeCommit <- findBaseTreeCommit(github, commitInfo.gitHubRepo, commitSha, headers)
        maybeBaseTreeCommitSha = maybeBaseTreeCommit.map(_.tree.sha)
        treeDataList <- createTreeDataList(github, commitInfo.gitHubRepo, baseDir, allFiles.toList, isText, headers)
        treeResult   <- createTree(github, commitInfo.gitHubRepo, maybeBaseTreeCommitSha, treeDataList, headers)
        refCommit    <- createCommit(
                          github,
                          commitInfo.gitHubRepo,
                          commitInfo.commitMessage,
                          Data.TreeResultSha.fromTreeResult(treeResult),
                          parentCommitSha,
                          headers,
                        )
      } yield Data.CommitSha(refCommit.sha).some
    }

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
  private[github] def commitAndPush0[F[_]: Fx: CanCatch: Monad: Temporal: Log](
    client: Client[F],
    gitHubRepoWithAuth: Data.GitHubRepoWithAuth,
    branch: Data.Branch,
    baseDir: Data.BaseDir,
    commitMessage: Data.CommitMessage,
    allDirs: NonEmptyVector[File],
    isText: Data.IsText,
    headers: Map[String, String],
  )(implicit githubConfig: GithubConfig): F[Either[GitHubError, Option[Ref]]] = (for {
    github <- EitherT.rightT[F, GitHubError](Github[F](client, gitHubRepoWithAuth.accessToken.map(_.accessToken)))
    commitInfo = Data.CommitInfo(gitHubRepoWithAuth.gitHubRepo, branch, commitMessage)
    allFiles  <-
      log(
        EitherT(FileF.getAllFiles(allDirs.toVector))
          .leftMap(GitHubError.fileHandling("getting all files to update commit dir"))
      )(
        err => error(GitHubError.render(err)),
        files => {
          val message =
            if (files.isEmpty)
              s"No files in ${baseDir.baseDir.getCanonicalPath} so skip committing"
            else
              files.mkString("[\n  ", "\n  ", "\n]")
          debug(s"Files to commit: $message")
        },
      )
    refCommit <- updateCommitFiles(github, commitInfo, baseDir, allFiles, isText, none[Data.CommitSha], headers)
    headRef   <-
      refCommit.traverse(commitSha => updateHead(github, gitHubRepoWithAuth.gitHubRepo, branch, commitSha, headers))
  } yield headRef).value

  @SuppressWarnings(Array("org.wartremover.warts.ExplicitImplicitTypes"))
  def commitAndPush[F[_]: Fx: CanCatch: Monad: Log: Temporal: Concurrent](
    client: Client[F],
    gitHubRepoWithAuth: Data.GitHubRepoWithAuth,
    branch: Data.Branch,
    baseDir: Data.BaseDir,
    commitMessage: Data.CommitMessage,
    allDirs: NonEmptyVector[File],
    isText: Data.IsText,
    headers: Map[String, String],
  )(gitHubApiConfig: GitHubApiConfig): F[Either[GitHubError, Option[Ref]]] = {
    implicit val githubConfig = GitHubApiConfig.toGithubConfig(gitHubApiConfig)
    commitAndPush0(client, gitHubRepoWithAuth, branch, baseDir, commitMessage, allDirs, isText, headers)
  }

  @SuppressWarnings(Array("org.wartremover.warts.ImplicitParameter"))
  def doesBranchExist[F[_]: Fx: Monad: Concurrent](
    client: Client[F],
    gitHubRepoWithAuth: Data.GitHubRepoWithAuth,
    branch: Data.Branch,
  )(implicit githubConfig: GithubConfig): F[Boolean] = {
    import github4s.Decoders._
    val httpClient =
      new HttpClient[F](client, githubConfig, new StaticAccessToken(gitHubRepoWithAuth.accessToken.map(_.accessToken)))
    (for {
      branchResponse <-
        httpClient
          .get[Branch](
            s"repos/${gitHubRepoWithAuth.gitHubRepo.org.org}/${gitHubRepoWithAuth.gitHubRepo.repo.repo}/branches/${branch.branch}",
          )
          .rightT[GHError]
      result         <- branchResponse.result.eitherT[F]
    } yield result.name === branch.branch).getOrElse(false)
  }

}
