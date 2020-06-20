---
id: config
title: Config
---

Add set `GitHubPagesPlugin` in the `build.sbt`.

## Essential settings
You probably need only these three setting keys in your project config to publish GitHub Pages.
```scala
gitHubPagesOrgName := "USERNAME_OR_ORG",
gitHubPagesRepoName := "YOUR_PROJECT",
gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"
```

e.g.)
```scala
lazy val root = (project in file("."))
  .enablePlugins(GitHubPagesPlugin)
  .settings(
    name := "YOUR_PROJECT",
    gitHubPagesOrgName := "USERNAME_OR_ORG",
    gitHubPagesRepoName := "YOUR_PROJECT",
    gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"
  )
```

:::note

Documentation Work in Progress

:::
