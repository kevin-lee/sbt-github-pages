package githubpages

import java.io.File

import githubpages.github.Data.GitHubApiConfig
import githubpages.github.GitHubApi
import sbt._

/**
 * @author Kevin Lee
 * @since 2020-05-26
 */
trait GitHubPagesKeys {

  val defaultDirNamesShouldBeIgnored: Set[String] = Set("target", "bin", "output")

  lazy val gitHubPagesGitHubBaseUrl: SettingKey[String] =
    settingKey[String](
      "The GitHub base URL for GitHub Enterprise. No need to set if you're using github.com. " +
        s"(default: ${GitHubApiConfig.default.baseUrl.baseUrl})"
    )

  lazy val gitHubPagesGitHubAuthorizeUrl: SettingKey[String] =
    settingKey[String](
      "The GitHub authorize URL for GitHub Enterprise. No need to set if you're using github.com. " +
        s"(default: ${GitHubApiConfig.default.authorizeUrl.authorizeUrl})"
    )

  lazy val gitHubPagesGitHubAccessTokenUrl: SettingKey[String] =
    settingKey[String](
      "The GitHub access token URL for GitHub Enterprise. No need to set if you're using github.com. " +
        s"(default: ${GitHubApiConfig.default.accessTokenUrl.accessTokenUrl})"
    )

  lazy val gitHubPagesGitHubHeaders: SettingKey[Map[String, String]] =
    settingKey[Map[String, String]](
      "The headers to add when sending a request to GitHub Enterprise API. No need to set if you're using github.com. " +
        s"(default: ${GitHubApiConfig.default.headers.headers})"
    )


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

  lazy val gitHubPagesDirsToIgnore: SettingKey[Set[String]] =
    settingKey[Set[String]](
      s"A list of directory names to be ignored when committing to the GitHub Pages branch. (default: ${defaultDirNamesShouldBeIgnored.mkString("[", ",", "]")})"
    )


  lazy val gitHubPagesIgnoreDotDirs: SettingKey[Boolean] =
    settingKey[Boolean](
      s"A flag to indicate whether to ignore or not dot directories when committing to the GitHub Pages branch. (default: true so any dot dirs (e.g. .cache, .github, .idea, etc.) are excluded when committing)"
    )

  lazy val gitHubPagesAcceptedTextExtensions: SettingKey[Set[String]] =
    settingKey[Set[String]](
      s"Accepted text files to create UTF-8 encoded String based blob. If the file's extension is not one of these, Base64 encoded blob is created. (default: ${GitHubApi.defaultTextExtensions.mkString("[", ",", "]")})"
    )

  lazy val gitHubPagesAcceptedTextMaxLength: SettingKey[Int] =
    settingKey[Int](
      s"The max length of the bytes (Array[Byte]) of the file to commit. If the file byte size is greater than this, Base64 encoded blob is created. (default: ${GitHubApi.defaultMaximumLength})"
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
