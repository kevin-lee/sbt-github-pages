import scala.language.implicitConversions

lazy val check = taskKey[Unit]("Check GitHubPagesPlugin's default settings under sbt 2")

def expect[A](name: String, actual: A, expected: A): Option[String] =
  if (actual == expected) None
  else Option(s"$name: expected [$expected] but got [$actual]")

lazy val root = (project in file("."))
  .enablePlugins(GitHubPagesPlugin)
  .settings(
    name                             := "sbt-2-defaults",
    scalaVersion                     := "3.8.4",
    gitHubPagesOrgName               := "kevin-lee",
    gitHubPagesRepoName              := "sbt-github-pages",
    gitHubPagesSiteDir               := baseDirectory.value / "site",
    /* Exercises the `durationInt` conversion from GitHubPagesPlugin.autoImport. */
    gitHubPagesPublishRequestTimeout := 3.minutes,
    /* sbt 2 caches every task by default, and FiniteDuration has no HashWriter. */
    check                            := Def.uncached {
      val failures = List(
        expect("gitHubPagesBranch", gitHubPagesBranch.value, "gh-pages"),
        expect("gitHubPagesNoJekyll", gitHubPagesNoJekyll.value, true),
        expect("gitHubPagesDirsToIgnore", gitHubPagesDirsToIgnore.value, Set("target", "bin", "output")),
        expect("gitHubPagesIgnoreDotDirs", gitHubPagesIgnoreDotDirs.value, true),
        expect("gitHubPagesAcceptedTextMaxLength", gitHubPagesAcceptedTextMaxLength.value, 10240),
        expect(
          "gitHubPagesAcceptedTextExtensions",
          gitHubPagesAcceptedTextExtensions.value,
          Set(".md", ".css", ".html", ".xml", ".js", ".txt"),
        ),
        expect(
          "gitHubPagesUseGithubTokenForGitHubPagesBranchCreation",
          gitHubPagesUseGithubTokenForGitHubPagesBranchCreation.value,
          true,
        ),
        expect("gitHubPagesPublishRequestTimeout", gitHubPagesPublishRequestTimeout.value, 3.minutes),
        expect("gitHubPagesOrgName", gitHubPagesOrgName.value, "kevin-lee"),
        expect("gitHubPagesRepoName", gitHubPagesRepoName.value, "sbt-github-pages"),
        expect("gitHubPagesSiteDir", gitHubPagesSiteDir.value, baseDirectory.value / "site"),
      ).flatten

      if (failures.nonEmpty) sys.error(failures.mkString("\n"))
    },
  )
