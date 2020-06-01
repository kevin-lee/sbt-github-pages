package githubpages

import java.io.File

import sbt._

/**
 * @author Kevin Lee
 * @since 2020-05-26
 */
trait GitHubPagesKeys {

  lazy val gitHubPagesBranch: SettingKey[String] =
    settingKey[String]("The GitHub Pages branch (default: gh-pages)")

  lazy val gitHubPagesNoJekyll: SettingKey[Boolean] =
    settingKey[Boolean]("The flag to decide whether to add .nojekyll (default: true)")

  lazy val gitHubPagesOrgName: SettingKey[String] =
    settingKey[String]("The GitHub organization name (or username) (e.g. https://github.com/OrgName/RepoName)")

  lazy val gitHubPagesRepoName: SettingKey[String] =
    settingKey[String]("The GitHub project repository name (e.g. https://github.com/OrgName/RepoName)")

  lazy val gitHubPagesGitHubToken: SettingKey[Option[String]] =
    settingKey[Option[String]](
      "The GitHub authentication token (default: The value from environment variable 'GITHUB_TOKEN')"
    )

  lazy val gitHubPagesSiteDir: SettingKey[File] =
    settingKey[File](
      "The folder contains all the files to be pushed to the GitHub Pages branch specified at gitHubPagesBranch"
    )

  lazy val gitHubPagesPublishCommitMessage: SettingKey[String] =
    settingKey[String](
      "The commit message when publish to GitHub Pages. " +
        "First, it tries to get the value from the environment variable 'GITHUB_PAGES_PUBLISH_COMMIT_MESSAGE'. " +
        s"""If not found, the default value is "Updated $${gitHubPagesBranch.value}""""
    )

  lazy val publishToGitHubPages: TaskKey[Unit] =
    taskKey[Unit](
      "Task to push the files specified at gitHubPagesSiteDir to the branch specified at gitHubPagesBranch."
    )

}
