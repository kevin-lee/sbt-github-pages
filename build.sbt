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
    pluginCrossBuild / sbtVersion := props.GlobalSbtVersion,
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

    val catsVersion       = "2.6.0"
    val catsEffectVersion = "2.5.0"
    val github4sVersion   = "0.28.4"
    val circeVersion      = "0.13.0"

    val http4sVersion: String = "0.21.22"

    val effectieVersion = "1.10.0"
    val loggerFVersion  = "1.10.0"
  }

lazy val libs =
  new {
    lazy val hedgehogLibs = List(
      "qa.hedgehog" %% "hedgehog-core"   % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-runner" % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-sbt"    % props.hedgehogVersion % Test,
    )

    lazy val cats        = "org.typelevel" %% "cats-core"    % props.catsVersion
    lazy val catsEffect  = "org.typelevel" %% "cats-effect"  % props.catsEffectVersion
    lazy val github4s    = "com.47deg"     %% "github4s"     % props.github4sVersion
    lazy val circeParser = "io.circe"      %% "circe-parser" % props.circeVersion

    lazy val http4sDsl    = "org.http4s" %% "http4s-dsl"          % props.http4sVersion
    lazy val http4sClient = "org.http4s" %% "http4s-blaze-client" % props.http4sVersion

    lazy val effectie          = "io.kevinlee" %% "effectie-cats-effect" % props.effectieVersion
    lazy val loggerFCatsEffect = "io.kevinlee" %% "logger-f-cats-effect" % props.loggerFVersion
    lazy val loggerFSbtLogging = "io.kevinlee" %% "logger-f-sbt-logging" % props.loggerFVersion

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
