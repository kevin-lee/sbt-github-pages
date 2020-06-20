import java.nio.charset.Charset

import ProjectInfo._
import kevinlee.sbt.SbtCommon._
import just.semver.SemVer
import sbt.ScmInfo
import sbt.io.{IO => SbtIo}
import _root_.cats.effect.IO

val ProjectScalaVersion: String = "2.12.11"
val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

val GlobalSbtVersion: String = "1.3.12"

val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

val hedgehogVersion: String = "bd4e0cc785915e0af20d2a7ead5267d49b1de7b1"

val hedgehogRepo: Resolver =
  "bintray-scala-hedgehog" at "https://dl.bintray.com/hedgehogqa/scala-hedgehog"

val hedgehogLibs: Seq[ModuleID] = Seq(
    "qa.hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  , "qa.hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  , "qa.hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test
  )

val cats: ModuleID = "org.typelevel" %% "cats-core" % "2.1.1"
val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % "2.1.3"
val github4s: ModuleID = "com.47deg" %% "github4s" % "0.24.0"

val http4sVersion: String = "0.21.3"
val http4sDsl: ModuleID = "org.http4s" %% "http4s-dsl" % http4sVersion
val http4sClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % http4sVersion

val effectie: ModuleID = "io.kevinlee" %% "effectie-cats-effect" % "1.0.0"
val loggerFCatsEffect: ModuleID = "io.kevinlee" %% "logger-f-cats-effect" % "0.3.1"

lazy val prepareDocusaurusBuild: TaskKey[Unit] =
  taskKey[Unit]("Task to do some preparation for docusaurus build.")

lazy val root = (project in file("."))
  .enablePlugins(DevOopsGitReleasePlugin, GitHubPagesPlugin)
  .settings(
    organization := "io.kevinlee"
  , name         := "sbt-github-pages"
  , scalaVersion := ProjectScalaVersion
  , version      := ProjectVersion
  , description  := "sbt plugin to publish GitHub Pages"
  , developers   := List(
      Developer("Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee"))
    )
  , homepage := Some(url("https://github.com/Kevin-Lee/sbt-github-pages"))
  , scmInfo :=
      Some(ScmInfo(
        url("https://github.com/Kevin-Lee/sbt-github-pages")
      , "git@github.com:Kevin-Lee/sbt-github-pages.git"
    ))

  , startYear := Some(2020)
  , sbtPlugin := true
  , sbtVersion in Global := GlobalSbtVersion
  , crossSbtVersions := CrossSbtVersions
  , scalacOptions ++= crossVersionProps(commonScalacOptions, SemVer.parseUnsafe(scalaVersion.value)) {
        case (SemVer.Major(2), SemVer.Minor(12)) =>
          Seq("-Ywarn-unused-import", "-Ywarn-numeric-widen")
        case (SemVer.Major(2), SemVer.Minor(11)) =>
          Seq("-Ywarn-numeric-widen")
        case _ =>
          Nil
      }
  , scalacOptions in (Compile, console) := scalacOptions.value diff List("-Ywarn-unused-import", "-Xfatal-warnings")
  , wartremoverErrors in (Compile, compile) ++= commonWarts
  , wartremoverErrors in (Test, compile) ++= commonWarts
  , resolvers += hedgehogRepo
  , addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
  , addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  , libraryDependencies ++= Seq(cats, catsEffect, github4s, http4sDsl, http4sClient, effectie, loggerFCatsEffect) ++ hedgehogLibs
  , testFrameworks ++= Seq(TestFramework("hedgehog.sbt.Framework"))

  /* GitHub Release { */
  , gitTagFrom := "main"
  , artifactsRequiredForGitHubRelease := false
  , devOopsPackagedArtifacts := List.empty[String]
  /* } GitHub Release */
  /* Publish { */
  , publishMavenStyle := false
  , licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

  , bintrayPackageLabels := Seq("sbt", "plugin")
  , bintrayVcsUrl := Some("""https://github.com/Kevin-Lee/sbt-github-pages""")
  , bintrayRepository := "sbt-plugins"
  /* } Publish */

  /* Docs { */
  , gitHubPagesOrgName := "Kevin-Lee"
  , gitHubPagesRepoName := "sbt-github-pages"
  , gitHubPagesSiteDir := (ThisBuild / baseDirectory).value / "website/build"
  , gitHubPagesAcceptedTextExtensions += ".js"

  , prepareDocusaurusBuild := Def.taskDyn {
      val algoliaConfigFileName = sys.env.getOrElse("ALGOLIA_CONFIG_FILENAME", "algolia.config.json")
      val algoliaConfigPath = (ThisBuild / baseDirectory).value / s"website/$algoliaConfigFileName"
      val algoliaApiKey = sys.env.get("ALGOLIA_API_KEY")
      val algoliaIndexName = sys.env.get("ALGOLIA_INDEX_NAME")

      implicit lazy val log: Logger = streams.value.log

      def logAndWriteFile(algoliaConfigFile: File, content: String)(logMessage: String)(implicit logger: Logger): IO[Unit] =
        for {
          _ <- IO(logger.info(logMessage))
          _ <- IO(SbtIo.write(algoliaConfigFile, content, Charset.forName("UTF-8"), append = false))
        } yield ()

      val createAlgoliaConfig = (algoliaApiKey, algoliaIndexName) match {
        case (Some(apiKey), Some(indexName)) =>
          // TODO: replace this logic with Validation
          if (apiKey.isEmpty && indexName.isEmpty)
            logAndWriteFile(algoliaConfigPath, "{}")(
              s"""The algoliaConfig info is found in environment variables but both values are empty.
                 |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
                 |""".stripMargin
            )
          else if (apiKey.isEmpty)
            logAndWriteFile(algoliaConfigPath, "{}")(
              s"""The algoliaConfig info is found in environment variables but apiKey value is empty.
                 |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
                 |""".stripMargin
            )
          else if (indexName.isEmpty)
            logAndWriteFile(algoliaConfigPath, "{}")(
              s"""The algoliaConfig info is found in environment variables but indexName value is empty.
                 |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
                 |""".stripMargin
            )
          else
            logAndWriteFile(
              algoliaConfigPath
              , s"""{
                   |  "apiKey": "$apiKey",
                   |  "indexName": "$indexName"
                   |}
                   |""".stripMargin)(
              s"""The algoliaConfig info is found so the algoliaConfig file will be generated at $algoliaConfigPath
                 |""".stripMargin
            )
        case (Some(_), None) =>
          logAndWriteFile(algoliaConfigPath, "{}")(
            s"""The algolia apiKey is found but no indexName is in the environment variables.
               |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
               |If you want to set up algolia, set the following env var.
               |  - ALGOLIA_INDEX_NAME
               |""".stripMargin
          )

        case (None, Some(_)) =>
          logAndWriteFile(algoliaConfigPath, "{}")(
            s"""The algolia indexName is found but no apiKey is in the environment variables.
               |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
               |If you want to set up algolia, set the following env var.
               |  - ALGOLIA_API_KEY
               |""".stripMargin
          )

        case (None, None) =>
          logAndWriteFile(algoliaConfigPath, "{}")(
            s"""No algoliaConfig (Optional) info found in the environment variables.
               |So It will create the algoliaConfig file with an empty algoliaConfig at $algoliaConfigPath
               |If you want to set up algolia, set the following env vars.
               |  - ALGOLIA_API_KEY
               |  - ALGOLIA_INDEX_NAME
               |""".stripMargin
          )
      }
      Def.task(createAlgoliaConfig.unsafeRunSync())
    }.value
  /* } Docs */

)
