---
id: getting-started
title: Getting Started
sidebar_label: Getting Started
---

## ![](../img/sbt-github-pages-logo-64x64.png) sbt-github-pages
sbt plugin to publish GitHub Pages with minimal effort.

[![Build Status](https://github.com/Kevin-Lee/sbt-github-pages/workflows/Build%20All/badge.svg)](https://github.com/Kevin-Lee/sbt-github-pages/actions?workflow=Build+All)
[![Release Status](https://github.com/Kevin-Lee/sbt-github-pages/workflows/Release/badge.svg)](https://github.com/Kevin-Lee/sbt-github-pages/actions?workflow=Release)
[ ![Download](https://api.bintray.com/packages/kevinlee/sbt-plugins/sbt-github-pages/images/download.svg) ](https://bintray.com/kevinlee/sbt-plugins/sbt-github-pages/_latestVersion)

![Cats Friendly Badge](https://typelevel.org/cats/img/cats-badge-tiny.png) 

## Get sbt-github-pages

In the `project/plugins.sbt`, add the following line,
```sbt
addSbtPlugin("io.kevinlee" % "sbt-github-pages" % "0.1.3")
```

In your `build.sbt`,
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

To publish `gh-pages`, run the following task in sbt console
```sbtshell
sbt:test-project> publishToGitHubPages 
```
or in bash or zsh
```bash
sbt publishToGitHubPages 
```


## Why sbt-github-pages?

So why `sbt-github-pages`? There's already the sbt plugin called [`sbt-ghpages`](https://github.com/sbt/sbt-ghpages) which does the same thing.

If you're using `sbt-ghpages`, that's good and you don't need to change it at all. `sbt-ghpages` is a great sbt plugin.

Difference between `sbt-github-pages` and `sbt-ghpages` is that `sbt-github-pages` uses GitHub API to push whereas `sbt-ghpages` uses `git` to push.

So to use `sbt-github-pages`, the only requirement is GitHub API access token which means you don't need to set anything if you're using GitHub Actions to publish since GitHub provides the access token in GitHub Actions build.

:::note

So `sbt-github-pages` works well with GitHub Actions.

:::
