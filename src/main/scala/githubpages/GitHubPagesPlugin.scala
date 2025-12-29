package githubpages

import cats.data.{EitherT, NonEmptyVector}
import cats.effect.{Concurrent, IO, Temporal}
import cats.syntax.all.*
import effectie.core.Fx
import effectie.syntax.all.*
import extras.cats.syntax.either.*
import filef.FileF
import github4s.domain.Ref
import githubpages.github.Data.GitHubApiConfig
import githubpages.github.{Data, GitHubApi, GitHubError}
import loggerf.core.Log as LogF
import loggerf.logger.*
import loggerf.syntax.all.*
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import sbt.Keys.{sLog, streams}
import sbt.{IO as SbtIo, *}

import scala.util.control.NonFatal

/** @author Kevin Lee
  * @since 2020-05-26
  */
object GitHubPagesPlugin extends AutoPlugin {

  override def requires: Plugins      = empty
  override def trigger: PluginTrigger = noTrigger

  object autoImport extends GitHubPagesKeys
  import autoImport.*

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def failWithMessage(message: String): Unit =
    throw new MessageOnlyException(message)

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def returnOrThrowMessageOnlyException[A, B](
    aOrB: Either[A, B]
  )(
    aToString: A => String
  ): B =
    aOrB.fold(a => throw new MessageOnlyException(aToString(a)), identity)

  private def pushToGhPages[F[_]: Fx: Temporal: Concurrent: LogF](
    client: Client[F],
    gitHubApiConfig: GitHubApiConfig,
    gitHubRepo: Data.GitHubRepoWithAuth,
    gitHubPagesBranch: Data.GitHubPagesBranch,
    commitMessage: Data.CommitMessage,
    siteDir: Data.SiteDir,
    dirFilter: File => Boolean,
    isText: Data.IsText,
    headers: Map[String, String],
    forcePush: Boolean,
  ): F[Either[GitHubError, Option[Ref]]] =
    EitherT(
      FileF.getAllDirsRecursively(
        siteDir.siteDir,
        dirFilter = dirFilter
      )
    ).leftMap {
      case NonFatal(err) =>
        GitHubError.nonFatalThrowable("Error fetching files recursively", err)
    }.flatMap {
      case Vector() =>
        pureOf(
          GitHubError
            .messageOnly(
              s"No files to commit in the dir at ${siteDir.siteDir.getCanonicalPath}"
            )
            .asLeft[Option[Ref]]
        ).eitherT

      case x +: xs =>
        for {
          dirs <- pureOf(NonEmptyVector(x, xs)).rightT
          _    <- FileF
                    .getAllFiles[F](dirs.toVector)
                    .eitherT
                    .leftMap(
                      GitHubError.fileHandling(
                        s"getting all files in ${dirs.toVector.mkString("[", ",", "]")} to push to GitHub Pages"
                      )
                    )
                    .log(
                      err => error(GitHubError.render(err)),
                      files =>
                        info(
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
                        headers,
                        forcePush
                      )(gitHubApiConfig)
                    )
        } yield result
    }.value

  private def decodeJsonToMapOfStringToString(message: String)(jsonString: String): Map[String, String] = {
    import _root_.io.circe.parser.*

    def errorMessage[A](err: A) =
      s"""Invalid JSON $message
         | - ERROR: $err
         | - Input: $jsonString
         """.stripMargin

    val headers = returnOrThrowMessageOnlyException(parse(jsonString))(errorMessage).as[Map[String, String]]
    returnOrThrowMessageOnlyException(headers)(errorMessage)
  }

  private lazy val forcePushStr = sys.env.getOrElse("GITHUB_PAGES_PUBLISH_FORCE_PUSH", "")

  override lazy val globalSettings: Seq[Def.Setting[_]] = Seq(
    gitHubPagesBranch               := "gh-pages",
    gitHubPagesNoJekyll             := true,
    gitHubPagesPublishCommitMessage :=
      sys.env.getOrElse("GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE", s"Updated ${gitHubPagesBranch.value}"),
    gitHubPagesPublishForcePush     :=
      forcePushStr == "1" || forcePushStr.equalsIgnoreCase("true")
  )

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    gitHubPagesGitHubBaseUrl      := sys.env.getOrElse("GITHUB_ENT_BASE_URL", GitHubApiConfig.default.baseUrl.baseUrl),
    gitHubPagesGitHubAuthorizeUrl := sys
      .env
      .getOrElse("GITHUB_ENT_AUTHORIZE_URL", GitHubApiConfig.default.authorizeUrl.authorizeUrl),
    gitHubPagesGitHubAccessTokenUrl                       := sys
      .env
      .getOrElse("GITHUB_ENT_ACCESS_TOKEN_URL", GitHubApiConfig.default.accessTokenUrl.accessTokenUrl),
    gitHubPagesGitHubHeaders                              :=
      sys
        .env
        .get("GITHUB_ENT_HEADERS")
        .fold(GitHubApiConfig.default.headers.headers)(
          decodeJsonToMapOfStringToString("for the environment variable 'GITHUB_ENT_HEADERS'")
        ),
    gitHubPagesGitHubToken                                := sys.env.get("GITHUB_TOKEN"),
    gitHubPagesDirsToIgnore                               := defaultDirNamesShouldBeIgnored,
    gitHubPagesIgnoreDotDirs                              := true,
    gitHubPagesAcceptedTextExtensions                     := GitHubApi.defaultTextExtensions,
    gitHubPagesAcceptedTextMaxLength                      := GitHubApi.defaultMaximumLength,
    gitHubPagesOrgName                                    := gitRemoteInfo._1,
    gitHubPagesRepoName                                   := gitRemoteInfo._2,
    gitHubPagesPublishRequestTimeout                      := DefaultGitHubPagesPublishRequestTimeout,
    gitHubPagesBranchExists                               := {
      val gitHubPagesBranchValue     = gitHubPagesBranch.value
      import sys.process.*
      val doesGitHubPagesBranchExist =
        s"git ls-remote --exit-code --heads origin $gitHubPagesBranchValue >/dev/null 2>&1;".! === 0
      doesGitHubPagesBranchExist
    },
    gitHubPagesGetCurrentBranch                           := {
      import sys.process.*
      val currentBranch =
        sys
          .env
          .get("GITHUB_HEAD_REF")
          .orElse(sys.env.get("GITHUB_REF_NAME"))
          .getOrElse("git rev-parse --abbrev-ref HEAD".!!.trim)
      currentBranch
    },
    gitHubPagesUseGithubTokenForGitHubPagesBranchCreation := true,
    gitHubPagesCreateGitHubPagesBranchIfNotExist          := {
      val logger        = sLog.value
      val ghPagesBranch = gitHubPagesBranch.value
      val currentBranch = gitHubPagesGetCurrentBranch.value

      val useGhToken = gitHubPagesUseGithubTokenForGitHubPagesBranchCreation.value

      if (gitHubPagesBranchExists.value) {
        logger.info(s">> The GitHub Pages branch (`$ghPagesBranch`) already exists so ignore creating it")
      } else {

        if (useGhToken) {
          if (sys.env.get("GITHUB_TOKEN").map(_.trim).exists(_.nonEmpty)) {
            logger.info(">> ✅ The required GITHUB_TOKEN environment variable is set.")
          } else {
            val errorMessage =
              raw""">> ❌ The required GITHUB_TOKEN environment variable is not set. Please set it.
                   |e.g.)
                   |      - name: Publish to GitHub Pages
                   |        env:
                   |          GITHUB_TOKEN: $${{ secrets.GITHUB_TOKEN }}
                   |""".stripMargin

            failWithMessage(errorMessage)
          }
        } else {
          logger.info(
            ">> ✅ gitHubPagesUseGithubTokenForGitHubPagesBranchCreation is `false` so ignore checking the availability of `GITHUB_TOKEN`"
          )
        }

        logger.info(s">> Current branch=$currentBranch")

        import sys.process.*

        // echo ">> Setting up git user"
        val setGitUserNameCmd = """git config user.name "github-actions[bot]""""
        logger.info(s">> Running: $setGitUserNameCmd")
        logger.info(setGitUserNameCmd.!!)

        val setGitUserEmailCmd = """git config user.email "github-actions[bot]@users.noreply.github.com""""
        logger.info(s">> Running: $setGitUserEmailCmd")
        logger.info(setGitUserEmailCmd.!!)

        // echo ">> Running: `git checkout --orphan gh-pages`"
        // git checkout --orphan gh-pages
        val gitCheckoutOrphanCmd = s"git checkout --orphan $ghPagesBranch"
        logger.info(s">> Running: $gitCheckoutOrphanCmd")
        logger.info(gitCheckoutOrphanCmd.!!)

        // echo ">> Running: rm -rf ."
        // git rm -rf .
        val gitRmRfDotCmd = "git rm -rf ."
        logger.info(s">> Running: $gitRmRfDotCmd")
        logger.info(gitRmRfDotCmd.!!)

        // echo '>> Running: git commit --allow-empty -m "Add gh-pages branch"'
        // git commit --allow-empty -m "Add gh-pages branch"
        val gitCommitAllowEmptyCmd = List("git", "commit", "--allow-empty", "-m", s"Add $ghPagesBranch branch")
        logger.info(s">> Running: ${gitCommitAllowEmptyCmd.mkString(" ")}")
        logger.info(gitCommitAllowEmptyCmd.!!)

        // echo ">> Running: gh auth setup-git"
        // gh auth setup-git
        val ghAuthSetupGitCmd = "gh auth setup-git"
        logger.info(s">> Running: $ghAuthSetupGitCmd")
        logger.info(ghAuthSetupGitCmd.!!)

        // echo ">> Running: git push origin gh-pages"
        // git push origin gh-pages

        val gitPushOriginCmd = List("git", "push", "origin", ghPagesBranch)
        logger.info(s">> Running: ${gitPushOriginCmd.mkString(" ")}")
        logger.info(gitPushOriginCmd.!!)

        // echo ">> Switching back to $current_branch"
        // git checkout "$current_branch"
        val gitCheckoutCurrentBranchCmd = List("git", "checkout", currentBranch)
        logger.info(s">> Running: ${gitCheckoutCurrentBranchCmd.mkString(" ")}")
        logger.info(gitCheckoutCurrentBranchCmd.!!)
      }

    },
    publishToGitHubPages := Def.taskDyn {

      gitHubPagesCreateGitHubPagesBranchIfNotExist.value

      val gitHubPagesPublishBranch = Data.GitHubPagesBranch(gitHubPagesBranch.value)

      val siteDir        = Data.SiteDir(gitHubPagesSiteDir.value)
      val noJekyll       = gitHubPagesNoJekyll.value
      val gitHubRepoOrg  = Data.GitHubRepo.Org(gitHubPagesOrgName.value)
      val gitHubRepoRepo = Data.GitHubRepo.Repo(gitHubPagesRepoName.value)
      val gitHubToken    = gitHubPagesGitHubToken.value.map(Data.GitHubRepoWithAuth.AccessToken)
      val commitMessage  = Data.CommitMessage(gitHubPagesPublishCommitMessage.value)
      val dirsToIgnore   = gitHubPagesDirsToIgnore.value
      val ignoreDotDirs  = gitHubPagesIgnoreDotDirs.value
      val forcePush      = gitHubPagesPublishForcePush.value
      @SuppressWarnings(Array("org.wartremover.warts.PlatformDefault"))
      val dirFilter      =
        if (ignoreDotDirs)
          (dir: File) => !(dirsToIgnore.contains(dir.getName.toLowerCase) || dir.getName.startsWith("."))
        else
          (dir: File) => !dirsToIgnore.contains(dir.getName.toLowerCase)

      val textExtensions = gitHubPagesAcceptedTextExtensions.value
      val textMaxLength  = gitHubPagesAcceptedTextMaxLength.value
      val blobConfig     = Data.BlobConfig(textExtensions, textMaxLength)

      val gitHubApiConfig: GitHubApiConfig = GitHubApiConfig(
        baseUrl = GitHubApiConfig.BaseUrl(gitHubPagesGitHubBaseUrl.value),
        authorizeUrl = GitHubApiConfig.AuthorizeUrl(gitHubPagesGitHubAuthorizeUrl.value),
        accessTokenUrl = GitHubApiConfig.AccessTokenUrl(gitHubPagesGitHubAccessTokenUrl.value),
        headers = GitHubApiConfig.Headers(gitHubPagesGitHubHeaders.value)
      )

      import cats.effect.unsafe.implicits.global
      import effectie.instances.ce3.fx.ioFx

      implicit val log: CanLog = SbtLogger.sbtLoggerCanLog(streams.value.log)

      if (gitHubToken.nonEmpty) {
        val requestTimeout           = gitHubPagesPublishRequestTimeout.value
        @SuppressWarnings(Array("org.wartremover.warts.Throw"))
        val publishToGitHubPagesTask = Def.task {
          (for {
            commitMessage <- IO.pure(commitMessage)
            _             <- IO(
                               log.info(
                                 s"""Committing files from ${siteDir.siteDir.getCanonicalPath}
                                    |  into the branch to publish GitHub Pages (i.e. '${gitHubPagesPublishBranch.gitHubPagesBranch}')
                                    |  * repo: ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo}
                                    |  * commit-message: ${commitMessage.commitMessage}
                                    |""".stripMargin
                               )
                             )
            result        <-
              BlazeClientBuilder[IO]
                .withRequestTimeout(requestTimeout)
                .resource
                .use { client =>
                  (for {
                    gitHubRepo       <-
                      IO.pure(Data.GitHubRepoWithAuth(Data.GitHubRepo(gitHubRepoOrg, gitHubRepoRepo), gitHubToken))
                        .rightT[GitHubError]
                    pageBranchExists <-
                      GitHubApi
                        .doesBranchExist[IO](
                          client,
                          gitHubRepo,
                          Data.Branch(gitHubPagesPublishBranch.gitHubPagesBranch)
                        )
                        .rightT[GitHubError]

                    _      <- if (pageBranchExists) IO.unit.rightT
                              else
                                IO(GitHubError.pagePublishBranchNotExist(gitHubRepo.gitHubRepo, gitHubPagesPublishBranch))
                                  .leftT[Unit]
                    _      <- (if (noJekyll) IO(SbtIo.touch(siteDir.siteDir / ".nojekyll")) else IO.unit).rightT
                    result <- pushToGhPages[IO](
                                client,
                                gitHubApiConfig,
                                gitHubRepo,
                                gitHubPagesPublishBranch,
                                commitMessage,
                                siteDir,
                                dirFilter,
                                GitHubApi.buildIsText(blobConfig),
                                GitHubApi.essentialHeaders,
                                forcePush
                              ).eitherT
                  } yield result).value
                }
          } yield result).unsafeRunSync() match {
            case Right(Some(ref)) =>
              log.info(
                s"Successfully pushed to ${gitHubPagesPublishBranch.gitHubPagesBranch} at ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo} " +
                  s"- Ref.Sha: ${ref.`object`.sha}"
              )

            case Right(None) =>
              failWithMessage(
                s"There was nothing to push to ${gitHubPagesPublishBranch.gitHubPagesBranch} at ${gitHubRepoOrg.org}/${gitHubRepoRepo.repo}"
              )

            case Left(error) =>
              failWithMessage(GitHubError.render(error))
          }
        }
        publishToGitHubPagesTask
      } else {
        Def.task(
          failWithMessage(
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

  /** Gets the Github user and repository from the git remote info */
  private val gitRemoteInfo = {
    import scala.sys.process.*

    val identifier  = """([^\/]+?)"""
    val GitHubHttps = s"https://github.com/$identifier/$identifier(?:\\.git)?".r
    val GitHubGit   = s"git://github.com:$identifier/$identifier(?:\\.git)?".r
    val GitHubSsh   = s"git@github.com:$identifier/$identifier(?:\\.git)?".r
    try {
      val remote = List("git", "ls-remote", "--get-url", "origin").!!.trim()
      remote match {
        case GitHubHttps(user, repo) => (user, repo)
        case GitHubGit(user, repo) => (user, repo)
        case GitHubSsh(user, repo) => (user, repo)
        case _ => ("", "")
      }
    } catch {
      case NonFatal(_) => ("", "")
    }
  }

}
