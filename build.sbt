import ProjectInfo.*

ThisBuild / organization       := props.Org
ThisBuild / scalaVersion       := props.ProjectScalaVersion
ThisBuild / crossScalaVersions := props.CrossScalaVersions
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

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin, DevOopsGitHubReleasePlugin)
  .settings(
    name             := props.ProjectName,
    description      := "sbt plugin to publish GitHub Pages",
    crossScalaVersions := props.CrossScalaVersions,

    /* Cross-build { */
    /* Scala 2.12 -> sbt 1.x, Scala 3 -> sbt 2.x.
     * The build itself is driven by sbt 1 (see project/build.properties); sbt's `sbtPluginExtra`
     * turns a `pluginCrossBuild / sbtVersion` of 2.x into the `_sbt2_3` artifact suffix.
     */
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => props.Sbt1Version
        case _      => props.Sbt2Version
      }
    },
    /* Unified `Def.uncached` (and friends) across sbt 1 and sbt 2.
     * Note this belongs in build.sbt, not project/plugins.sbt: it is a dependency of the
     * plugin being built, not of the build itself.
     */
    addSbtPlugin(libs.sbt2Compat),
    scalacOptions ++= (if (scalaBinaryVersion.value == "2.12") List.empty else props.Scala3ScalacOptions),
    /* } Cross-build */

    Compile / console / scalacOptions ~= (options => options diff List("-Ywarn-unused-import", "-Xfatal-warnings")),
    Compile / compile / wartremoverErrors ++= commonWarts,
    Test / compile / wartremoverErrors ++= commonWarts,
    libraryDependencies ++= libs.all,
    testFrameworks ++= Seq(TestFramework("hedgehog.sbt.Framework")),

    /* Scripted { */
    scriptedLaunchOpts := scriptedLaunchOpts.value ++ Seq("-Xmx1024M", "-Dplugin.version=" + version.value),
    scriptedBufferLog  := false,
    /* } Scripted */

    /* GitHub Release { */
    devOopsPackagedArtifacts := List.empty[String],
    /* } GitHub Release */

    /* Publish { */
    publishMavenStyle := true,
    licenses          := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    /* } Publish */

  )
