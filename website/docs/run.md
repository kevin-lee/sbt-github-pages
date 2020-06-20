---
id: run
title: Run to Publish GitHub Pages
sidebar_label: Run to Publish
---

## Publish GitHub Pages

:::important

Before publish, make sure your project repo has the `gh-pages` branch.
If it does not exist, create it first. Creating the `gh-pages` branch
 might be added to `sbt-github-pages` in the future, but it is not available at the moment.

:::

### Create gh-pages branch
You need to do it only once. If your project repo already has the `gh-pages` branch, skip it.

```bash
# Move to some temporary location.
# Clone your project. This one will be removed once the `gh-pages` creation is done.
$ git clone git@github.com:USERNAME/YOUR_PROJECT.git

$ cd YOUR_PROJECT

$ git checkout --orphan gh-pages

# Make sure you're in the project directory.
$ rm -Rf .

$ git commit --allow-empty -m "Add gh-pages branch"

$ git push origin gh-pages

# Now you can remove this project in the temporary location.
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
