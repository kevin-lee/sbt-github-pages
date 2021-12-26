package githubpages.github

import java.io.File

import cats._
import cats.syntax.all._

import github4s.GithubConfig
import github4s.domain.TreeResult

/** @author Kevin Lee
  * @since 2020-05-27
  */
object Data {

  final case class GitHubApiConfig(
    baseUrl: GitHubApiConfig.BaseUrl,
    authorizeUrl: GitHubApiConfig.AuthorizeUrl,
    accessTokenUrl: GitHubApiConfig.AccessTokenUrl,
    headers: GitHubApiConfig.Headers
  )
  object GitHubApiConfig {

    final case class BaseUrl(baseUrl: String) extends AnyVal
    final case class AuthorizeUrl(authorizeUrl: String) extends AnyVal
    final case class AccessTokenUrl(accessTokenUrl: String) extends AnyVal
    final case class Headers(headers: Map[String, String]) extends AnyVal

    implicit val equal: Eq[GitHubApiConfig] = Eq.fromUniversalEquals[GitHubApiConfig]

    def default: GitHubApiConfig =
      GitHubApiConfig(
        baseUrl = GitHubApiConfig.BaseUrl(GithubConfig.default.baseUrl),
        authorizeUrl = GitHubApiConfig.AuthorizeUrl(GithubConfig.default.authorizeUrl),
        accessTokenUrl = GitHubApiConfig.AccessTokenUrl(GithubConfig.default.accessTokenUrl),
        headers = GitHubApiConfig.Headers(GithubConfig.default.headers)
      )

    def toGithubConfig(gitHubApiConfig: GitHubApiConfig): GithubConfig = {
      if (gitHubApiConfig === default)
        GithubConfig.default
      else
        GithubConfig(
          baseUrl = gitHubApiConfig.baseUrl.baseUrl,
          authorizeUrl = gitHubApiConfig.authorizeUrl.authorizeUrl,
          accessTokenUrl = gitHubApiConfig.accessTokenUrl.accessTokenUrl,
          headers = gitHubApiConfig.headers.headers
        )
    }

  }

  final case class GitHubRepo(
    org: GitHubRepo.Org,
    repo: GitHubRepo.Repo,
  )

  object GitHubRepo {

    final case class Org(org: String) extends AnyVal
    final case class Repo(repo: String) extends AnyVal

  }

  final case class GitHubRepoWithAuth(
    gitHubRepo: GitHubRepo,
    accessToken: Option[GitHubRepoWithAuth.AccessToken]
  )

  object GitHubRepoWithAuth {

    final case class AccessToken(accessToken: String) {
      override val toString: String = "*** Protected ***"
    }

  }

  final case class GitHubPagesBranch(gitHubPagesBranch: String) extends AnyVal

  final case class SiteDir(siteDir: File) extends AnyVal

  final case class BaseDir(baseDir: File) extends AnyVal

  final case class Branch(branch: String) extends AnyVal

  final case class CommitMessage(commitMessage: String) extends AnyVal

  final case class CommitInfo(gitHubRepo: GitHubRepo, branch: Branch, commitMessage: CommitMessage)

  final case class CommitSha(commitSha: String) extends AnyVal

  final case class TreeResultSha(treeResultSha: String) extends AnyVal
  object TreeResultSha {
    def fromTreeResult(treeResult: TreeResult): TreeResultSha =
      TreeResultSha(treeResult.sha)
  }

  final case class BlobConfig(acceptedExtensions: Set[String], maxLength: Int)

  final case class IsText(blobConfig: BlobConfig) extends ((File, Array[Byte]) => Boolean) {

    override def apply(file: File, bytes: Array[Byte]): Boolean =
      blobConfig.acceptedExtensions.exists(extension => file.getName.toLowerCase.endsWith(extension)) &&
        bytes.length < blobConfig.maxLength

    override lazy val toString: String =
      s"""${scala.runtime.ScalaRunTime._toString(this)}
         |  A function to determine if the given file with the bytes
         |  can be a text based blob when committing.
         |  It is a text one if the follow two conditions are met.
         |    - The filename ends with one of these extensions. ${blobConfig.acceptedExtensions.mkString("[", ",", "]")}
         |    - the byte length (Array[Byte].length) of the file is less than ${blobConfig.maxLength}
         |""".stripMargin

  }

}
