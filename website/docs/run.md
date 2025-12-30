---
id: run
title: Run to Publish GitHub Pages
sidebar_label: Run to Publish
---

## Publish GitHub Pages


:::warning IMPORTANT!

Make sure to run
```
gitHubPagesCreateGitHubPagesBranchIfNotExist
```
before any publish task
to make sure the `gh-pages` branch exists. If the `gh-pages` branch doesn't exist,
it will create the `gh-pages` branch.
:::

### Create gh-pages branch

<details>
  <summary>Legacy: Manual <code>gh-pages</code> branch creation (not recommended)</summary>

:::warning NOTE
It is no longer required as `gitHubPagesCreateGitHubPagesBranchIfNotExist` task will handle it automatically.
:::

You need to do it only once. If your project repo already has the `gh-pages` branch, skip it.

```bash
# Move to some temporary location.
# Clone your project. This one will be removed once the `gh-pages` creation is done.
$ git clone git@github.com:USERNAME/YOUR_PROJECT.git

$ cd YOUR_PROJECT

$ git checkout --orphan gh-pages

# Make sure you're in the project directory.
$ git rm -rf .

$ git commit --allow-empty -m "Add gh-pages branch"

$ git push origin gh-pages

# Now you can remove this project in the temporary location.
```

</details>

In GitHub Actions, creating `gh-pages` would be as easy as running `gitHubPagesCreateGitHubPagesBranchIfNotExist` task.

e.g.)

When the `gh-pages` branch is already there, 
```bash
sbt gitHubPagesCreateGitHubPagesBranchIfNotExist
```
```
[info] >> The GitHub Pages branch (`gh-pages`) already exists so ignore creating it
```

When the `gh-pages` branch is missing,
```bash
sbt gitHubPagesCreateGitHubPagesBranchIfNotExist
```
```sbtshell
[info] >> ✅ The required GITHUB_TOKEN environment variable is set.
[info] >> Current branch=your-current-branch-name
[info] >> Running: git config user.name "github-actions[bot]"
[info] >> Running: git config user.email "github-actions[bot]@users.noreply.github.com"
[info] >> Running: git checkout --orphan gh-pages
Switched to a new branch 'gh-pages'
[info] >> Running: git rm -rf .
[info] rm '.github/dependabot.yml'
[info] rm '.github/pr-labeler.yml'
[info] rm '.github/workflows/build.yml'
[info] rm '.github/workflows/doc-site-publish.yml'
[info] rm '.gitignore'
[info] rm 'LICENSE'
[info] rm 'README.md'
[info] >> Running: git commit --allow-empty -m Add gh-pages branch
[info] [gh-pages (root-commit) f947f53] Add gh-pages branch
[info] >> Running: gh auth setup-git
[info] >> Running: git push origin gh-pages
remote: 
remote: Create a pull request for 'gh-pages' on GitHub by visiting:        
remote:      https://github.com/your_username/your-project/pull/new/gh-pages        
remote: 
To https://github.com/your_username/your-project
 * [new branch]      gh-pages -> gh-pages
[info] >> Running: git checkout your-current-branch-name
Switched to branch 'your-current-branch-name'
```


### Publish GitHub Pages

To publish `gh-pages`, run the following task in sbt console
```sbtshell
sbt:test-project> publishToGitHubPages 
```
or in bash or zsh
```bash
sbt publishToGitHubPages 
```

e.g.)
```
sbt:test-project> publishToGitHubPages
[info] Committing files from /path/to/test-project/website/build
[info]   into the branch to publish GitHub Pages (i.e. 'gh-pages')
[info]   * repo: USERNAME/test-project
[info]   * commit-message: Updated gh-pages
[info] The following allDirs will be pushed to 'gh-pages' branch.
[info]   - /path/to/test-project/website/build/main.js
[info]   - /path/to/test-project/website/build/styles.js
[info]   - /path/to/test-project/website/build/404.html
[info]   - /path/to/test-project/website/build/index.html
[info]   - /path/to/test-project/website/build/.nojekyll
[info]   - /path/to/test-project/website/build/styles.css
[info]   - /path/to/test-project/website/build/sitemap.xml
[info]   - /path/to/test-project/website/build/docs/index.html
[info]   - /path/to/test-project/website/build/img/poster.png
[info]   - /path/to/test-project/website/build/img/logo.png
[info]   - /path/to/test-project/website/build/img/favicon.png
[info]   - /path/to/test-project/website/build/docs/run/index.html
[info]   - /path/to/test-project/website/build/docs/config/index.html
[success] Total time: 7 s, completed 20 Jun. 2020, 11:34:18 pm
```

:::info
Please check out [this example GitHub Actions](/docs/examples)
to learn how to use it with GitHub Actions.
:::

