---
id: examples
title: Examples
sidebar_label: Examples
---

## Docusaurus + GitHub Actions
```
Project
Root ─┬─ project
      ├─ src
      ├─ ... more directories
      └─ website─┬─ blog
                 ├─ docs
                 ├─ build <== Contains website files
                 ├─ node_modules
                 └─ ... more directories
```


### plugins.sbt

In the `project/plugins.sbt`

```sbt
addSbtPlugin("io.kevinlee" % "sbt-github-pages" % "0.1.3")
```


### build.sbt

In `build.sbt`,

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

### GitHub Actions
In your GitHub Actions config file,

e.g.) `.github/workflows/publish-github-pages.yml`

```yaml
name: Publish GitHub Pages

on:
  push:
    branches:
      - publish-docs

jobs:
  build:

    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v1
        with:
          node-version: '14.2.0'
          registry-url: 'https://registry.npmjs.org'

      - name: Cache Coursier
        uses: actions/cache@v1
        with:
          path: ~/.cache/coursier
          key: ${{ runner.os }}-coursier-scala-2_13-${{ hashFiles('**/*.sbt') }}-${{ hashFiles('**/build.properties') }}
          restore-keys: |
            ${{ runner.os }}-coursier-scala-2_13-

      - name: Cache Ivy
        uses: actions/cache@v1
        with:
          path: ~/.ivy2/cache
          key: ${{ runner.os }}-ivy-scala-2_13-${{ hashFiles('**/*.sbt') }}-${{ hashFiles('**/build.properties') }}
          restore-keys: |
            ${{ runner.os }}-ivy-scala-2_13-

      - name: Cache npm
        uses: actions/cache@v1
        with:
          path: ~/.npm
          key: ${{ runner.os }}-node-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-node-

      - name: Build and publish website using Docusaurus
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: |
          cd website
          echo "> Install packages"
          npm install
          echo "> Build the website using Docusaurus"
          rm -Rf build
          npm run build
          cd ..
          echo "> Publish to GitHub Pages"
          sbt publishToGitHubPages

```
Pushing to `publish-docs` branch triggers the publish build in GitHub Actions.
