package githubpages

import cats.data.{EitherT, NonEmptyVector}
import cats.effect.{ConcurrentEffect, ContextShift, IO, Timer, Sync => CatsSync}
import cats.implicits._

import effectie.cats.EitherTSupport._
import effectie.cats.{CanCatch, EffectConstructor}

import filef.FileF

import github4s.domain.Ref
import githubpages.github.{Data, GitHubApi, GitHubError}

import loggerf.cats.Log.LeveledMessage._
import loggerf.cats.Logful._
import loggerf.cats.{Log => LogF}
import loggerf.sbt.SbtLogger

import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import sbt.Keys.streams
import sbt.{IO => SbtIo, _}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/**
 * @author Kevin Lee
 * @since 2020-05-26
 */
object GitHubPagesPlugin extends AutoPlugin {

  override def requires: Plugins = empty
  override def trigger: PluginTrigger = noTrigger

  object autoImport extends GitHubPagesKeys
  import autoImport._

  private def pushToGhPages[F[_]: EffectConstructor: CanCatch: CatsSync: ConcurrentEffect: Timer: LogF](
    client: Client[F],
    gitHubRepo: Data.GitHubRepoWithAuth,
    gitHubPagesBranch: Data.GitHubPagesBranch,
    commitMessage: Data.CommitMessage,
    siteDir: Data.SiteDir,
    dirFilter: File => Boolean,
    isText: Data.IsText,
    headers: Map[String, String]
  ): F[Either[GitHubError, Option[Ref]]] =
    EitherT(
      FileF.getAllDirsRecursively(
        siteDir.siteDir,
        dirFilter = dirFilter
      )
    ).leftMap {
      case NonFatal(err) =>
        GitHubError.nonFatalThrowable("Error fetching files recursively", err)
    }
    .flatMap {
      case Vector() =>
        eitherTOfPure(
          GitHubError.messageOnly(
            s"No files to commit in the dir at ${siteDir.siteDir.getCanonicalPath}"
          ).asLeft[Option[Ref]]
        )

      case x +: xs =>
          for {
            dirs <- eitherTRightPure(NonEmptyVector(x, xs))
            _ <- log(
                EitherT(FileF.getAllFiles[F](dirs.toVector))
                  .leftMap(GitHubError.fileHandling(s"getting all files in ${dirs.toVector.mkString("[", ",", "]")} to push to GitHub Pages"))
              )(
                err => error(GitHubError.render(err)),
                files => info(
                    s"""The following allDirs will be pushed to '${gitHubPagesBranch.gitHubPagesBranch}' branch.
                       |${files.mkString("  - ", "\n  - ", "")}
                       |""".stripMargin
                  )
              )
            result <- EitherT(
                GitHubApi.commitAndPush[F](
                  client,
                  gitHubRepo,
                  Data.Branch(gitHubPagesBranch.gitHubPagesBranch),
                  Data.BaseDir(siteDir.siteDir),
                  commitMessage,
                  dirs,
                  isText,
                  headers
                )
              )
          } yield result
    }.value

  override lazy val globalSettings: Seq[Def.Setting[_]] = Seq(
    gitHubPagesBranch := "gh-pages",
    gitHubPagesNoJekyll := true,
    gitHubPagesPublishCommitMessage :=
      sys.env.getOrElse("GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE", s"Updated ${gitHubPagesBranch.value}"),
  )

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    gitHubPagesGitHubToken := sys.env.get("GITHUB_TOKEN"),
    gitHubPagesDirsToIgnore := defaultDirNamesShouldBeIgnored,
    gitHubPagesIgnoreDotDirs := true,
    gitHubPagesAcceptedTextExtensions := GitHubApi.defaultTextExtensions,
    gitHubPagesAcceptedTextMaxLength := GitHubApi.defaultMaximumLength,
    publishToGitHubPages := Def.taskDyn {

      val siteDir = Data.SiteDir(gitHubPagesSiteDir.value)
      val noJekyll = gitHubPagesNoJekyll.value
      val gitHubPagesPublishBranch = Data.GitHubPagesBranch(gitHubPagesBranch.value)
      val gitHubRepoOrg = Data.GitHubRepo.Org(gitHubPagesOrgName.value)
      val gitHubRepoRepo = Data.GitHubRepo.Repo(gitHubPagesRepoName.value)
      val gitHubToken = gitHubPagesGitHubToken.value.map(Data.GitHubRepoWithAuth.AccessToken)
      val commitMessage = Data.CommitMessage(gitHubPagesPublishCommitMessage.value)
      val dirsToIgnore = gitHubPagesDirsToIgnore.value
      val ignoreDotDirs = gitHubPagesIgnoreDotDirs.value
      val dirFilter =
        if (ignoreDotDirs)
          (dir: File) => !(dirsToIgnore.contains(dir.getName.toLowerCase) || dir.getName.startsWith("."))
        else
          (dir: File) => !dirsToIgnore.contains(dir.getName.toLowerCase)

      val textExtensions = gitHubPagesAcceptedTextExtensions.value
      val textMaxLength = gitHubPagesAcceptedTextMaxLength.value
      val blobConfig = Data.BlobConfig(textExtensions, textMaxLength)

      implicit val ec: ExecutionContext = ExecutionContext.global
      implicit val cs: ContextShift[IO] = IO.contextShift(ec)
      implicit val timer: Timer[IO] = IO.timer(ec)

      implicit val log: loggerf.Logger = SbtLogger.sbtLogger(streams.value.log)

      if (gitHubToken.nonEmpty) {
        @SuppressWarnings(Array("org.wartremover.warts.Throw"))
        val publishToGitHubPagesTask = Def.task {
          (for {
            commitMessage <- IO.pure(commitMessage)
            _ <- IO(
                log.info(
                  s"""Committing files from ${siteDir.siteDir.getCanonicalPath}
                     |  into the branch to publish GitHub Pages (i.e. '${gitHubPagesPublishBranch.gitHubPagesBranch}')
                     |  * repo: ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo}
                     |  * commit-message: ${commitMessage.commitMessage}
                     |""".stripMargin
                )
              )
            result <- BlazeClientBuilder[IO](ec).resource.use { client =>
                for {
                  gitHubRepo <- IO.pure(Data.GitHubRepoWithAuth(Data.GitHubRepo(gitHubRepoOrg, gitHubRepoRepo), gitHubToken))
                  _ <- if (noJekyll) IO(SbtIo.touch(siteDir.siteDir / ".nojekyll")) else IO.unit
                  result <- pushToGhPages[IO](
                      client,
                      gitHubRepo,
                      gitHubPagesPublishBranch,
                      commitMessage,
                      siteDir,
                      dirFilter,
                      GitHubApi.buildIsText(blobConfig),
                      GitHubApi.essentialHeaders
                    )
                } yield result
              }
          } yield result).unsafeRunSync() match {
            case Right(Some(ref)) =>
              log.info(s"Successfully pushed to ${gitHubPagesPublishBranch.gitHubPagesBranch} at ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo} - Ref.Sha: ${ref.`object`.sha}")

            case Right(None) =>
              log.info(s"There was nothing to push to ${gitHubPagesPublishBranch.gitHubPagesBranch} at ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo}")

            case Left(error) =>
              throw new MessageOnlyException(GitHubError.render(error))
          }
        }
        publishToGitHubPagesTask
      } else {
        Def.task(
          log.error(
            s"""
               |You must provide a GitHub token through
               |the environment variable named 'GITHUB_TOKEN' for pushing GitHub Pages.
               |If you are using GitHub Actions, in your GitHub Actions workflow yaml file,
               |you can just do something like
               |
               |    - name: Publish GitHub Pages
               |      env:
               |        GITHUB_TOKEN: $${{ secrets.GITHUB_TOKEN }}
               |      run: sbt publishToGitHubPages
               |
               |""".stripMargin
          )
        )
      }
    }.value
  )

}
