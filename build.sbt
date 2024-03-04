import ProjectInfo._

ThisBuild / organization := props.Org
ThisBuild / scalaVersion := props.ProjectScalaVersion
ThisBuild / developers   := List(
  Developer(
    props.GitHubUsername,
    "Kevin Lee",
    "kevin.code@kevinlee.io",
    url(s"https://github.com/${props.GitHubUsername}")
  )
)

ThisBuild / homepage     := url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}").some

ThisBuild / scmInfo   := ScmInfo(
  url(s"https://github.com/${props.GitHubUsername}/${props.ProjectName}"),
  s"git@github.com:${props.GitHubUsername}/${props.ProjectName}.git",
).some

ThisBuild / startYear := 2020.some

ThisBuild / resolvers += "sonatype-snapshots" at s"https://${props.SonatypeCredentialHost}/content/repositories/snapshots"

Global / sbtVersion := props.GlobalSbtVersion

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin, DevOopsGitHubReleasePlugin, DocusaurPlugin)
  .settings(
    name                          := props.ProjectName,
    description                   := "sbt plugin to publish GitHub Pages",
    crossSbtVersions              := props.CrossSbtVersions,
    pluginCrossBuild / sbtVersion := props.GlobalSbtVersion,
    Compile / console / scalacOptions ~= (options => options diff List("-Ywarn-unused-import", "-Xfatal-warnings")),
    Compile / compile / wartremoverErrors ++= commonWarts,
    Test / compile / wartremoverErrors ++= commonWarts,
    libraryDependencies ++= libs.all,
    testFrameworks ++= Seq(TestFramework("hedgehog.sbt.Framework")),

    /* GitHub Release { */
    devOopsPackagedArtifacts := List.empty[String],
    /* } GitHub Release */

    /* Publish { */
    publishMavenStyle := true,
    licenses          := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    /* } Publish */

    /* Docs { */
    docusaurDir      := (ThisBuild / baseDirectory).value / "website",
    docusaurBuildDir := docusaurDir.value / "build",
    /* } Docs */

  )
  .settings(mavenCentralPublishSettings)

lazy val props =
  new {

    val SonatypeCredentialHost = "s01.oss.sonatype.org"
    val SonatypeRepository     = s"https://$SonatypeCredentialHost/service/local"

    private val gitHubRepo = findRepoOrgAndName

    final val Org = "io.kevinlee"

    final val ProjectScalaVersion       = "2.12.18"
    val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

    final val GitHubUsername = gitHubRepo.fold("Kevin-Lee")(_.orgToString)
    final val ProjectName    = gitHubRepo.fold("sbt-github-pages")(_.nameToString)

    final val GlobalSbtVersion = "1.6.2"

    val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

    final val hedgehogVersion = "0.10.1"

    final val CatsVersion       = "2.10.0"
    final val CatsEffectVersion = "3.5.3"
    final val Github4sVersion   = "0.32.1"
    final val CirceVersion      = "0.14.6"

    final val Http4sVersion            = "0.23.25"
    final val Http4sBlazeClientVersion = "0.23.16"

    final val EffectieVersion = "2.0.0-beta14"
    final val LoggerFVersion  = "2.0.0-beta24"

    final val ExtrasVersion = "0.44.0"
  }

lazy val libs =
  new {
    lazy val hedgehogLibs = List(
      "qa.hedgehog" %% "hedgehog-core"   % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-runner" % props.hedgehogVersion % Test,
      "qa.hedgehog" %% "hedgehog-sbt"    % props.hedgehogVersion % Test,
    )

    lazy val cats        = "org.typelevel" %% "cats-core"    % props.CatsVersion
    lazy val catsEffect  = "org.typelevel" %% "cats-effect"  % props.CatsEffectVersion
    lazy val github4s    = "com.47deg"     %% "github4s"     % props.Github4sVersion
    lazy val circeParser = "io.circe"      %% "circe-parser" % props.CirceVersion

    lazy val http4sDsl    = "org.http4s" %% "http4s-dsl"          % props.Http4sVersion
    lazy val http4sClient = "org.http4s" %% "http4s-blaze-client" % props.Http4sBlazeClientVersion

    lazy val effectie          = "io.kevinlee" %% "effectie-cats-effect3" % props.EffectieVersion
    lazy val loggerFCatsEffect = "io.kevinlee" %% "logger-f-cats"         % props.LoggerFVersion
    lazy val loggerFSbtLogging = "io.kevinlee" %% "logger-f-sbt-logging"  % props.LoggerFVersion

    lazy val extrasCats = "io.kevinlee" %% "extras-cats" % props.ExtrasVersion

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
        extrasCats,
      ) ++ hedgehogLibs
  }

lazy val mavenCentralPublishSettings: SettingsDefinition = List(
  /* Publish to Maven Central { */
  sonatypeCredentialHost := props.SonatypeCredentialHost,
  sonatypeRepository     := props.SonatypeRepository,
  /* } Publish to Maven Central */
)
