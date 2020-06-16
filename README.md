# sbt-github-pages
sbt plugin to publish GitHub Pages

[![Build Status](https://github.com/Kevin-Lee/sbt-github-pages/workflows/Build%20All/badge.svg)](https://github.com/Kevin-Lee/sbt-github-pages/actions?workflow=Build+All)
[![Release Status](https://github.com/Kevin-Lee/sbt-github-pages/workflows/Release/badge.svg)](https://github.com/Kevin-Lee/sbt-github-pages/actions?workflow=Release)
[ ![Download](https://api.bintray.com/packages/kevinlee/sbt-plugins/sbt-github-pages/images/download.svg) ](https://bintray.com/kevinlee/sbt-plugins/sbt-github-pages/_latestVersion)

# Get sbt-github-pages

**NOTE: sbt-github-pages hasn't been released yet**

In the `project/plugins.sbt`, add the following line,
```sbt
addSbtPlugin("io.kevinlee" % "sbt-github-pages" % "0.1.0")
```

# Set up sbt-github-pages

Add set `GitHubPagesPlugin` in the `build.sbt`.
```sbt
lazy val root = (project in file("."))
  .enablePlugins(GitHubPagesPlugin)
  .settings(
    name := "YOUR_PROJECT",
    gitHubPagesOrgName := "USERNAME_OR_ORG",
    gitHubPagesRepoName := "YOUR_PROJECT",
    gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"
  )

```

# Publish GitHub Pages

To publish `gh-pages`, run the following task in sbt console
```sbtshell
sbt:YOUR_PROJECT> publishToGitHubPages 
```
or
```bash
sbt publishToGitHubPages 
```

**More detailed docs will be added once it's released.**
