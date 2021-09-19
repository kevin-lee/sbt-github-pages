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
addSbtPlugin("io.kevinlee" % "sbt-github-pages" % "0.7.0")
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
  workflow_dispatch:

jobs:
  build_and_publish_doc_site:
    if: github.ref != 'refs/heads/gh-pages' && github.ref != 'gh-pages'

    runs-on: ubuntu-latest

    strategy:
      matrix:
        scala:
          - { binary-version: "2.12", java-version: "adopt@1.11" }

    steps:
      - uses: actions/checkout@v2.3.4
      - uses: olafurpg/setup-scala@v10
        with:
          java-version: ${{ matrix.scala.java-version }}
      - uses: actions/setup-node@v2.1.5
        with:
          node-version: '14.4.0'
          registry-url: 'https://registry.npmjs.org'

      - name: Cache SBT
        uses: actions/cache@v2
        with:
          path: |
            ~/.ivy2/cache
            ~/.cache/coursier
            ~/.sbt
          key: ${{ runner.os }}-sbt-${{ matrix.scala.binary-version }}-${{ hashFiles('**/*.sbt') }}
          restore-keys: |
            ${{ runner.os }}-sbt-${{ matrix.scala.binary-version }}-

      - name: Cache npm
        uses: actions/cache@v2
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
