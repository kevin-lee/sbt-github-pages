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
addSbtPlugin("io.kevinlee" % "sbt-github-pages" % "0.19.0")
```


### build.sbt

In `build.sbt`,

```scala
lazy val root = (project in file("."))
  .enablePlugins(GitHubPagesPlugin)
  .settings(
    name := "YOUR_PROJECT",
    gitHubPagesSiteDir := baseDirectory.value / "website"
  )
```

### GitHub Actions
In your GitHub Actions config file,

e.g.)

```yaml title=".github/workflows/publish-github-pages.yml"
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
          - { java-version: "21", java-distribution: "temurin" }
        node:
          - { version: "24.12.0" }

    steps:
      - uses: actions/checkout@v6
      - uses: actions/setup-java@v5
        with:
          java-version: ${{ matrix.scala.java-version }}
          distribution: ${{ matrix.scala.java-distribution }}
          cache: 'sbt'
      - uses: sbt/setup-sbt@v1
      - uses: actions/setup-node@v6
        with:
          node-version: ${{ matrix.node.version }}
          registry-url: 'https://registry.npmjs.org'

      - name: Cache npm
        uses: actions/cache@v5
        with:
          path: ~/.npm
          key: ${{ runner.os }}-node-${{ matrix.node.version }}-${{ hashFiles('**/package-lock.json') }}
          restore-keys: |
            ${{ runner.os }}-node-${{ matrix.node.version }}

      - name: Build and publish website using Docusaurus
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: |
          echo "> Create gh-pages branch if not exist"
          sbt gitHubPagesCreateGitHubPagesBranchIfNotExist
          
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
