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


### GitHub Org Name / Username *

:::important
This key must be set by the user of this plugin.
:::

| Name                 | Value Type | Default    |
| -------------------- | ---------- | ---------- |
| `gitHubPagesOrgName` | `Boolean`  |            |

The GitHub organization name (or username) (i.e.`OrgName` from `https://github.com/OrgName/RepoName`)

e.g.)
```scala
gitHubPagesOrgName := "Kevin-Lee"
```


### GitHub Repo Name *

:::important
This key must be set by the user of this plugin.
:::

| Name                  | Value Type | Default    |
| --------------------- | ---------- | ---------- |
| `gitHubPagesRepoName` | `Boolean`  |            |

The GitHub project repository name (i.e. `RepoName` from `https://github.com/OrgName/RepoName`)

e.g.)
```scala
gitHubPagesRepoName := "sbt-github-pages"
```


### Site Dir *

:::important
This key must be set by the user of this plugin.
:::

| Name                 | Value Type | Default                       |
| -------------------- | ---------- | ----------------------------- |
| `gitHubPagesSiteDir` | `File`     |                               |

The folder contains all the files to be pushed to the GitHub Pages branch specified at `gitHubPagesBranch`.

e.g.)

Project structure
```
Project
Root ─┬─ dirA
      ├─ dirB
      └─ website─┬─ dirC
                 ├─ dirD
                 ├─ build <== Contains website files
                 └─ node_modules
```

```scala
gitHubPagesSiteDir := (ThisBuild / baseDirectory).value / "website/build"
```

## More Settings


### GitHub Pages Branch

| Name                | Value Type | Default    |
| --------------------| ---------- | ---------- |
| `gitHubPagesBranch` | `String`   | `gh-pages` |

A setting key to set the GitHub Pages branch. By default, it's `gh-pages` but if your project repos uses a different branch you can set it up using this key.

e.g.) If you want to use `main` branch instead of `gh-pages`,
````scala
gitHubPagesBranch := "main"
````

### NoJekyll
| Name                  | Value Type | Default    |
| --------------------- | ---------- | ---------- |
| `gitHubPagesNoJekyll` | `Boolean`  | `true`     |

The flag to decide whether to add `.nojekyll`.

GitHub Pages, by default, uses Jekyll to generate a static website. However, `sbt-github-pages` is designed to publish a ready to go website to GitHub Pages. So it's necessary to tell GitHub Pages not to use Jeklly and ask to use whatever is sent to the GitHub Pages branch as is. It can be done by placing a `.nojekyll` file.

If you want GitHub Pages to use Jekyll and generate a static website, set `gitHubPagesNoJekyll` to `false`.

e.g.) Let GitHub Pages use Jekyll to generate a website.
```scala
gitHubPagesNoJekyll := false
```


### GitHubToken
| Name                     | Value Type       | Default                       |
| ------------------------ | ---------------- | ----------------------------- |
| `gitHubPagesGitHubToken` | `Option[String]` | `sys.env.get("GITHUB_TOKEN")` |

:::danger Warning!!!
DO NOT hard-code the actual GitHub auth token String here.
Use an environment variable instead.
:::

The GitHub authentication token (default: The value from environment variable `GITHUB_TOKEN`)
If you're using GitHub Actions to publish, you don't need to set it up at all.
GitHub provides the GitHub token in GitHub Actions build so you can just use it.

e.g.)
In your GitHub Actions config yml file,
```yml
jobs:
  build:

    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v2

# ...

      - name: Publish website
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        run: |
          echo "> Publish GitHub Pages"
          sbt publishToGitHubPages
```

e.g.) If you want to change it,
```scala
gitHubPagesGitHubToken := sys.env.get("SOME_OTHER_VAR")
```

:::danger Warning!!!
DO NOT hard-code the actual GitHub auth token String here.
Use an environment variable instead.
:::


### Dirs To Ignore
| Name                      | Value Type    | Default                          |
| ------------------------- | ------------- | -------------------------------- |
| `gitHubPagesDirsToIgnore` | `Set[String]` | `Set("target", "bin", "output")` |

A list of directory names to be ignored when committing to the GitHub Pages branch.

e.g.) You add more or remove some.
```scala
gitHubPagesDirsToIgnore := Set("node_modules", "build", "output")
```


### Ignore Dot Dirs
| Name                       | Value Type    | Default                          |
| -------------------------- | ------------- | -------------------------------- |
| `gitHubPagesIgnoreDotDirs` | `Boolean`     | `true`                           |
A flag to indicate whether to ignore or not dot directories when committing to the GitHub Pages branch.

e.g.) To include all the dot dirs like `.cache`, `.github`, `.idea`, etc., set it to `false`.
```scala
gitHubPagesIgnoreDotDirs := false
```


### Accepted Text Extensions
| Name                                | Value Type    | Default                                                                |
| ----------------------------------- | ------------- | ---------------------------------------------------------------------- |
| `gitHubPagesAcceptedTextExtensions` | `Set[String]` | `Set(".md", ".css", ".html", ".properties", ".txt", ".scala", ".sbt")` |

Accepted text files to create `UTF-8` encoded `String` based blob. 
If the file's extension is not one of these, `Base64` encoded blob is created.

Default:
```scala
Set(".md", ".css", ".html", ".properties", ".txt", ".scala", ".sbt")
```

e.g.) You add more or remove some.
```scala
gitHubPagesAcceptedTextExtensions :=
  Set(".json", ".js", ".css", ".html", ".md", ".txt")
```

### Accepted Text MaxLength
| Name                                | Value Type | Default |
| ----------------------------------- | ---------- | ------- |
| `gitHubPagesAcceptedTextMaxLength`  | `Int`      | `4048`  |

The max length of the bytes (`Array[Byte]`) of the file to commit. 
If the file byte size is greater than this, `Base64` encoded blob is created.

e.g.) You can make it bigger.
```scala
gitHubPagesAcceptedTextMaxLength := 6144
```

### Publish Commit Message
| Name                               | Value Type | Default                                                                               |
| ---------------------------------- | ---------- | ------------------------------------------------------------------------------------- |
| `gitHubPagesPublishCommitMessage`  | `String`   | ENV VAR `GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE` or `Updated ${gitHubPagesBranch.value}` |

The commit message when publish to GitHub Pages. 

First, it tries to get the value from the environment variable `GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE`. 
If not found, the default value is `s"Updated ${gitHubPagesBranch.value}"`

Default:
```scala
gitHubPagesPublishCommitMessage :=
  sys.env.getOrElse(
    "GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE",
    s"Updated ${gitHubPagesBranch.value}"
  )
```

e.g.) If you want to have a different message, you can change it.
```scala
gitHubPagesPublishCommitMessage := s"New stuff in my awesome website!!!"
```

