import ProjectInfo._

lazy val prepareDocusaurusBuild: TaskKey[Unit] = taskKey[Unit]("Task to do some preparation for docusaurus build.")

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin, DevOopsGitHubReleasePlugin, DocusaurPlugin)
  .settings(
    organization := "io.kevinlee",
    name := "sbt-github-pages",
    scalaVersion := props.ProjectScalaVersion,
    description := "sbt plugin to publish GitHub Pages",
    developers := List(
      Developer("Kevin-Lee", "Kevin Lee", "kevin.code@kevinlee.io", url("https://github.com/Kevin-Lee"))
    ),
    homepage := url("https://github.com/Kevin-Lee/sbt-github-pages").some,
    scmInfo :=
      ScmInfo(
        url("https://github.com/Kevin-Lee/sbt-github-pages"),
        "git@github.com:Kevin-Lee/sbt-github-pages.git",
      ).some,
    startYear := 2020.some,
    Global / sbtVersion := props.GlobalSbtVersion,
    crossSbtVersions := props.CrossSbtVersions,
    pluginCrossBuild / sbtVersion := "1.2.8",
    Compile / console / scalacOptions := scalacOptions.value diff List("-Ywarn-unused-import", "-Xfatal-warnings"),
    Compile / compile / wartremoverErrors ++= commonWarts,
    Test / compile / wartremoverErrors ++= commonWarts,
    libraryDependencies ++= libs.all,
    testFrameworks ++= Seq(TestFramework("hedgehog.sbt.Framework")),
    /* GitHub Release { */
    devOopsPackagedArtifacts := List.empty[String],
    /* } GitHub Release */
    /* Publish { */
    publishMavenStyle := true,
    licenses := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    /* } Publish */

    /* Docs { */
    docusaurDir := (ThisBuild / baseDirectory).value / "website",
    docusaurBuildDir := docusaurDir.value / "build",
    gitHubPagesOrgName := "Kevin-Lee",
    gitHubPagesRepoName := "sbt-github-pages",

    /* } Docs */

  )

lazy val props =
  new {
    val ProjectScalaVersion: String     = "2.12.12"
    val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

    val GlobalSbtVersion: String = "1.2.8"

    val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

    val hedgehogVersion: String = "0.6.7"
    val http4sVersion: String   = "0.21.22"
  }

lazy val libs =
  new {
    lazy val hedgehogLibs: Seq[ModuleID] = Seq(
      "qa.hedgehog" %% "hedgehog-core"   % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-runner" % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-sbt"    % props.hedgehogVersion % Test,
    )

    lazy val cats: ModuleID        = "org.typelevel" %% "cats-core"    % "2.6.0"
    lazy val catsEffect: ModuleID  = "org.typelevel" %% "cats-effect"  % "2.5.0"
    lazy val github4s: ModuleID    = "com.47deg"     %% "github4s"     % "0.28.4"
    lazy val circeParser: ModuleID = "io.circe"      %% "circe-parser" % "0.13.0"

    lazy val http4sDsl: ModuleID    = "org.http4s" %% "http4s-dsl"          % props.http4sVersion
    lazy val http4sClient: ModuleID = "org.http4s" %% "http4s-blaze-client" % props.http4sVersion

    lazy val effectie: ModuleID          = "io.kevinlee" %% "effectie-cats-effect" % "1.10.0"
    lazy val loggerFCatsEffect: ModuleID = "io.kevinlee" %% "logger-f-cats-effect" % "1.10.0"
    lazy val loggerFSbtLogging: ModuleID = "io.kevinlee" %% "logger-f-sbt-logging" % "1.10.0"

    lazy val all: List[ModuleID] =
      List(
        cats,
        catsEffect,
        github4s,
        circeParser,
        http4sDsl,
        http4sClient,
        effectie,
        loggerFCatsEffect,
        loggerFSbtLogging,
      ) ++ hedgehogLibs
  }
