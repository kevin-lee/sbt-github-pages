import devoops.DevOopsGitHubPlugin.autoImport.*
import sbt.*
import wartremover.{Wart, Warts}

object ProjectInfo {

  val commonWarts: Seq[Wart] =
    Warts.allBut(
      Wart.DefaultArguments,
      Wart.Overloading,
      Wart.Any,
      Wart.Nothing,
      Wart.NonUnitStatements
    )

  object props {

    private val gitHubRepo = findRepoOrgAndName

    val Org = "io.kevinlee"

    val ProjectScalaVersion  = "2.12.20"
    val ProjectScala3Version = "3.8.4"

    val CrossScalaVersions: Seq[String] = Seq(ProjectScalaVersion, ProjectScala3Version).distinct

    val GitHubUsername = gitHubRepo.fold("kevin-lee")(_.orgToString)
    val ProjectName    = gitHubRepo.fold("sbt-github-pages")(_.nameToString)

    /* The sbt version each Scala axis targets (see `pluginCrossBuild / sbtVersion`).
     * Sbt1Version is also the minimum sbt 1 version this plugin supports.
     */
    val Sbt1Version = "1.11.2"
    val Sbt2Version = "2.0.1"

    val Sbt2CompatVersion = "0.2.0"

    /* Scala-3-only options. The shared sources must keep compiling on Scala 2.12, so a
     * handful of Scala 3 warnings can't be fixed at the source; `-Xfatal-warnings` comes
     * from sbt-tpolecat, hence the suppressions.
     */
    val Scala3ScalacOptions: Seq[String] = Seq(
      // Scala 3 asks for `using` on explicit implicit applications that must stay valid on 2.12.
      "-Wconf:msg=Implicit parameters should be provided with a `using` clause:s",
      // The sbt 2 plugin classpath pulls in the Scala 3 stdlib; these two notices are classpath
      // artifacts, not our code.
      "-Wconf:msg=package scala contains object and package with same name:s",
      "-Wconf:msg=several versions of the Scala standard library:s",
      // Migration-style notices on code that must remain valid on Scala 2.12 too.
      "-Wconf:msg=The syntax `private\\[this\\]` will be deprecated:s",
      "-Wconf:msg=is eta-expanded even though:s",
      // `Vector()` / `x +: xs` matches that Scala 3's exhaustivity checker can't see through.
      "-Wconf:msg=match may not be exhaustive:s",
      // Scala 3 -Wunused flags context-bound type classes (used only via implicit machinery).
      "-Wconf:msg=unused implicit parameter:s",
      "-Wconf:msg=Modifier final is redundant:s",
      // The sbt DSL uses alphanumeric methods infix; Scala 3 warns on this.
      "-Wconf:msg=is not declared infix:s",
      // `import sbtcompat.PluginCompat.*` is only load-bearing on the sbt 1 axis.
      "-Wconf:msg=unused import:s",
      "-Wconf:msg=unused value of type:s",
      "-Wconf:msg=discarded non-Unit value:s",
    )

    val hedgehogVersion = "0.15.0"

    val CatsVersion       = "2.13.0"
    val CatsEffectVersion = "3.7.1"
    val Github4sVersion   = "0.33.3"
    val CirceVersion      = "0.14.16"

    val Http4sVersion = "0.23.37"

    val EffectieVersion = "2.5.0"

    /* logger-f-sbt-logging is versioned independently of logger-f since 2.11.0. Its Scala 3
     * build links against sbt 2's util-logging_3, which is what the sbt 2 axis needs.
     */
    val LoggerFVersion           = "2.14.0"
    val LoggerFSbtLoggingVersion = "2.14.0"

    val ExtrasVersion = "0.56.0"
  }

  object libs {
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
    lazy val http4sClient = "org.http4s" %% "http4s-ember-client" % props.Http4sVersion

    lazy val effectie          = "io.kevinlee" %% "effectie-cats-effect3" % props.EffectieVersion
    lazy val loggerFCatsEffect = "io.kevinlee" %% "logger-f-cats"         % props.LoggerFVersion
    lazy val loggerFSbtLogging = "io.kevinlee" %% "logger-f-sbt-logging"  % props.LoggerFSbtLoggingVersion

    lazy val extrasCats = "io.kevinlee" %% "extras-cats" % props.ExtrasVersion

    lazy val sbt2Compat = "com.github.sbt" % "sbt2-compat" % props.Sbt2CompatVersion

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

}
