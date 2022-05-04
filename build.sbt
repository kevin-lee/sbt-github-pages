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

    final val ProjectScalaVersion       = "2.12.12"
    val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion)

    final val GitHubUsername = gitHubRepo.fold("Kevin-Lee")(_.orgToString)
    final val ProjectName    = gitHubRepo.fold("sbt-github-pages")(_.nameToString)

    final val GlobalSbtVersion = "1.2.8"

    val CrossSbtVersions: Seq[String] = Seq(GlobalSbtVersion)

    final val hedgehogVersion = "0.8.0"

    final val catsVersion       = "2.7.0"
    final val catsEffectVersion = "3.3.5"
    final val github4sVersion   = "0.31.0"
    final val circeVersion      = "0.14.1"

    final val http4sVersion = "0.23.11"

    final val effectieVersion = "2.0.0-beta1"
    final val loggerFVersion  = "2.0.0-beta1"

    final val ExtrasVersion = "0.13.0"
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

    lazy val effectie          = "io.kevinlee" %% "effectie-cats-effect3" % props.effectieVersion
    lazy val loggerFCatsEffect = "io.kevinlee" %% "logger-f-cats" % props.loggerFVersion
    lazy val loggerFSbtLogging = "io.kevinlee" %% "logger-f-sbt-logging" % props.loggerFVersion

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
