import ProjectInfo._

lazy val prepareDocusaurusBuild: TaskKey[Unit] = taskKey[Unit]("Task to do some preparation for docusaurus build.")

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin, DevOopsGitHubReleasePlugin, DocusaurPlugin)
  .settings(
    organization := props.Org,
    name := props.ProjectName,
    scalaVersion := props.ProjectScalaVersion,
    description := "sbt plugin to publish GitHub Pages",
    developers := List(
      Developer(props.GitHubUsername, "Kevin Lee", "kevin.code@kevinlee.io", url(s"https://github.com/${props.GitHubUsername}"))
    ),
    homepage := url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}").some,
    scmInfo :=
      ScmInfo(
        url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}"),
        s"git@github.com:${props.GitHubUsername}/${props.ProjectName}.git",
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
    /* } Docs */

  )

lazy val props =
  new {
    private val gitHubRepo = findRepoOrgAndName

    final val Org = "io.kevinlee"

    final val ProjectScalaVersion     = "2.12.12"
    val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

    final val GitHubUsername         = gitHubRepo.fold("Kevin-Lee")(_.orgToString)
    final val ProjectName = gitHubRepo.fold("sbt-github-pages")(_.nameToString)

    final val GlobalSbtVersion = "1.2.8"

    val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

    final val hedgehogVersion = "0.7.0"

    final val catsVersion       = "2.6.1"
    final val catsEffectVersion = "2.5.3"
    final val github4sVersion   = "0.28.5"
    final val circeVersion      = "0.14.1"

    final val http4sVersion = "0.21.27"

    final val effectieVersion = "1.15.0"
    final val loggerFVersion  = "1.15.0"
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
