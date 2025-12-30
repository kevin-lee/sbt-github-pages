---
id: config
title: Config
---

Add set `GitHubPagesPlugin` in the `build.sbt`.

## Essential settings
You probably need only these three setting keys in your project config to publish GitHub Pages.
```scala
gitHubPagesSiteDir := baseDirectory.value / "path/to/github-pages-root"

// These aren't mandatory unless sbt-github-pages can't infer them from `git remote`
gitHubPagesOrgName := "USERNAME_OR_ORG",
gitHubPagesRepoName := "YOUR_PROJECT",
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
If this is not set, it will be inferred from the `git remote` information.

So it's a mandatory setting, but no manual setting is required if your git repo has the remote set up.
:::

| Name                 | Value Type | Default                                  |
| -------------------- | ---------- | ---------------------------------------- |
| `gitHubPagesOrgName` | `String`   | Value obtained from calling `git remote` |

The GitHub organization name (or username) (i.e.`OrgName` from `https://github.com/OrgName/RepoName`).

Most of the times you won't need to set this setting, since `sbt-github-pages` will try to read it from the `git remote` information.

e.g.)
```scala
gitHubPagesOrgName := "kevin-lee"
```


### GitHub Repo Name *

:::important
If this is not set, it will be inferred from the `git remote` information.

So it's a mandatory setting, but no manual setting is required if your git repo has the remote set up. 
:::

| Name                  | Value Type | Default                                  |
| --------------------- | ---------- | ---------------------------------------- |
| `gitHubPagesRepoName` | `String`   | Value obtained from calling `git remote` |

The GitHub project repository name (i.e. `RepoName` from `https://github.com/OrgName/RepoName`).

Most of the times you won't need to set this setting, since `sbt-github-pages` will try to read it from the `git remote` information.

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
gitHubPagesSiteDir := (ThisBuild / baseDirectory).value / "website" / "build"
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
| Name                                | Value Type    | Default                                              |
| ----------------------------------- | ------------- | ---------------------------------------------------- |
| `gitHubPagesAcceptedTextExtensions` | `Set[String]` | `Set(".md", ".css", ".html", ".xml", ".js", ".txt")` |

Accepted text files to create `UTF-8` encoded `String` based blob. 
If the file's extension is not one of these, `Base64` encoded blob is created.

Default:
```scala
Set(".md", ".css", ".html", ".xml", ".js", ".txt")
```

e.g.) You add more or remove some.
```scala
gitHubPagesAcceptedTextExtensions :=
  Set(".json", ".js", ".css", ".html")
```

### Accepted Text MaxLength
| Name                                | Value Type | Default  |
| ----------------------------------- | ---------- | -------- |
| `gitHubPagesAcceptedTextMaxLength`  | `Int`      | `10240`  |

The max length of the bytes (`Array[Byte]`) of the file to commit. 
If the file byte size is greater than this, `Base64` encoded blob is created.

e.g.) You can make it smaller.
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

### Publish Force Push
| Name                           | Value Type | Default                                                                               |
| ------------------------------ | ---------- | ------------------------------------------------------------------------------------- |
| `gitHubPagesPublishForcePush`  | `Boolean`  | ENV VAR `GITHUB_PAGES_PUBLISH_FORCE_PUSH` or false |

The commit message when publish to GitHub Pages. 

First, it tries to get the value from the environment variable `GITHUB_PAGES_PUBLISH_FORCE_PUSH`. 
If not found, the default value is false

Default:
```scala
gitHubPagesPublishCommitMessage :=
  sys.env.getOrElse(
    "GITHUB_PAGES_PUBLISH_FORCE_PUSH",
    false
  )
```

e.g.) If you want to, you can enable force push.
```scala
gitHubPagesPublishForcePush := true
```

## Use GitHub Enterprise
`sbt-github-pages` support GitHub Enterprise

There are four properties can be used to use GitHub Enterprise. 
To set these up, get the right values from your company.
* `gitHubPagesGitHubBaseUrl`
* `gitHubPagesGitHubAuthorizeUrl`
* `gitHubPagesGitHubAccessTokenUrl`
* `gitHubPagesGitHubHeaders`

### GitHub Enterprise - Base URL
| Name                       | Value Type | Default                                                    |
| -------------------------- | ---------- | ---------------------------------------------------------- |
| `gitHubPagesGitHubBaseUrl` | `String`   | ENV VAR `GITHUB_ENT_BASE_URL` or `https://api.github.com/` |

NOTE: The trailing slash is significant. So `https://some.url.blah` does not work. It should be `https://some.url.blah/`.

e.g.)
```shell
export GITHUB_ENT_BASE_URL="https://github.my-company.internal/api/v3/"
```
```scala
gitHubPagesGitHubBaseUrl := "https://github.my-company.internal/api/v3/"
```


### GitHub Enterprise - Authorize URL
| Name                            | Value Type | Default                                                                                                                         |
| ------------------------------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------- |
| `gitHubPagesGitHubAuthorizeUrl` | `String`   | ENV VAR `GITHUB_ENT_AUTHORIZE_URL` or `https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s` |

e.g.)
```shell
export GITHUB_ENT_AUTHORIZE_URL="https://github.my-company.internal/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"
```

```scala
gitHubPagesGitHubAuthorizeUrl :=
  "https://github.my-company.internal/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s"
```


### GitHub Enterprise - Access Token URL
| Name                              | Value Type | Default                                                                                |
| --------------------------------- | ---------- | -------------------------------------------------------------------------------------- |
| `gitHubPagesGitHubAccessTokenUrl` | `String`   | ENV VAR `GITHUB_ENT_ACCESS_TOKEN_URL` or `https://github.com/login/oauth/access_token` |

e.g.)
```shell
export GITHUB_ENT_ACCESS_TOKEN_URL="https://github.my-company.internal/login/oauth/access_token"
```

```scala
gitHubPagesGitHubAccessTokenUrl :=
  "https://github.my-company.internal/login/oauth/access_token"
```


### GitHub Enterprise - Headers
| Name                       | Value Type            | Default                                                           |
| -------------------------- | --------------------- | ----------------------------------------------------------------- |
| `gitHubPagesGitHubHeaders` | `Map[String, String]` | ENV VAR `GITHUB_ENT_HEADERS` or `Map("User-Agent" -> "github4s")` |

e.g.)
When using the environment variable, the value should be JSON containing String key to String value pairs.
e.g.)
```shell
export GITHUB_ENT_HEADERS='{"User-Agent":"app-doc-publisher", "something-else":"blah"}'
```

```scala
gitHubPagesGitHubHeaders := Map("User-Agent" -> "app-doc-publisher")
```
