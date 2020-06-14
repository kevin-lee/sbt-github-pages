package githubpages.github

import java.io.File

import github4s.domain.TreeResult

/**
 * @author Kevin Lee
 * @since 2020-05-27
 */
object Data {

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


  final case class IsBlob(blobConfig: BlobConfig) extends ((File, Array[Byte]) => Boolean) {

    override def apply(file: File, bytes: Array[Byte]): Boolean =
      blobConfig.acceptedExtensions.exists(
        extension => file.getName.toLowerCase.endsWith(extension)
      ) &&
        bytes.length < blobConfig.maxLength

    override def toString(): String =
      s"""${super.toString()}
         |A function to determine if the given file with the bytes can be a blob.
         |It is a blob if the follow two conditions are met.
         |  - The filename ends with one of these extensions. ${blobConfig.acceptedExtensions.mkString("[", ",", "]")}
         |  - the length of the bytes (Array[Byte]) is less than ${blobConfig.maxLength}
         |""".stripMargin

  }

}