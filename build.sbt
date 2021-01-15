import java.nio.charset.Charset

import ProjectInfo._
import kevinlee.sbt.SbtCommon._
import just.semver.SemVer
import sbt.ScmInfo

val ProjectScalaVersion: String = "2.12.12"
val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

val GlobalSbtVersion: String = "1.2.8"

val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

val hedgehogVersion: String = "0.6.1"

val hedgehogRepo: Resolver =
  "bintray-scala-hedgehog" at "https://dl.bintray.com/hedgehogqa/scala-hedgehog"

val hedgehogLibs: Seq[ModuleID] = Seq(
    "qa.hedgehog" %% "hedgehog-core" % hedgehogVersion % Test
  , "qa.hedgehog" %% "hedgehog-runner" % hedgehogVersion % Test
  , "qa.hedgehog" %% "hedgehog-sbt" % hedgehogVersion % Test
  )

val cats: ModuleID = "org.typelevel" %% "cats-core" % "2.2.0"
val catsEffect: ModuleID = "org.typelevel" %% "cats-effect" % "2.2.0"
val github4s: ModuleID = "com.47deg" %% "github4s" % "0.25.0"
val circeParser: ModuleID = "io.circe" %% "circe-parser" % "0.13.0"

val http4sVersion: String = "0.21.6"
val http4sDsl: ModuleID = "org.http4s" %% "http4s-dsl" % http4sVersion
val http4sClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % http4sVersion

val effectie: ModuleID = "io.kevinlee" %% "effectie-cats-effect" % "1.3.0"
val loggerFCatsEffect: ModuleID = "io.kevinlee" %% "logger-f-cats-effect" % "1.3.1"
val loggerFSbtLogging: ModuleID = "io.kevinlee" %% "logger-f-sbt-logging" % "1.3.1"

lazy val prepareDocusaurusBuild: TaskKey[Unit] =
  taskKey[Unit]("Task to do some preparation for docusaurus build.")

lazy val root = (project in file("."))
  .enablePlugins(DevOopsGitReleasePlugin, DocusaurPlugin)
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
  , pluginCrossBuild / sbtVersion := "1.2.8"
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
  , libraryDependencies ++= Seq(
      cats, catsEffect, github4s, circeParser, http4sDsl, http4sClient, effectie, loggerFCatsEffect, loggerFSbtLogging
    ) ++ hedgehogLibs
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
  , docusaurDir := (ThisBuild / baseDirectory).value / "website"
  , docusaurBuildDir := docusaurDir.value / "build"

  , gitHubPagesOrgName := "Kevin-Lee"
  , gitHubPagesRepoName := "sbt-github-pages"

  /* } Docs */

)
