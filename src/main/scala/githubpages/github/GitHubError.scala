package githubpages.github

import java.io.File
import errors.StackTraceToString
import filef.FileError
import github4s.GHError
import githubpages.github.Data.{Branch, GitHubPagesBranch, GitHubRepo}

import scala.io.AnsiColor

/** @author Kevin Lee
  * @since 2020-06-01
  */
sealed trait GitHubError

object GitHubError {
  final case class NonFatalThrowable private (message: String, throwable: Throwable) extends GitHubError
  object NonFatalThrowable {
    private[GitHubError] def apply(message: String, throwable: Throwable): NonFatalThrowable =
      new NonFatalThrowable(message, throwable)
  }

  final case class MessageOnly(message: String) extends GitHubError
  object MessageOnly {
    private[GitHubError] def apply(message: String): GitHubError =
      new MessageOnly(message)
  }

  final case class RelativePathNotFound(file: File) extends GitHubError
  object RelativePathNotFound {
    private[GitHubError] def apply(file: File): GitHubError =
      new RelativePathNotFound(file)
  }

  final case class RequestFailure(ghError: GHError) extends GitHubError
  object RequestFailure {
    private[GitHubError] def apply(ghError: GHError): GitHubError =
      new RequestFailure(ghError)
  }

  final case class FileHandling(forWhat: String, fileError: FileError) extends GitHubError
  object FileHandling {
    private[GitHubError] def apply(forWhat: String, fileError: FileError): GitHubError =
      new FileHandling(forWhat, fileError)
  }

  final case class BranchNotFound(gitHubRepo: GitHubRepo, branch: Branch) extends GitHubError
  object BranchNotFound {
    private[GitHubError] def apply(gitHubRepo: GitHubRepo, branch: Branch): GitHubError =
      new BranchNotFound(gitHubRepo, branch)
  }

  final case class PagePublishBranchNotExist(gitHubRepo: GitHubRepo, branch: GitHubPagesBranch) extends GitHubError

  def nonFatalThrowable(message: String, throwable: Throwable): GitHubError =
    NonFatalThrowable(message, throwable)

  def messageOnly(message: String): GitHubError = MessageOnly(message)

  def relativePathNotFound(file: File): GitHubError = RelativePathNotFound(file)

  def requestFailure(ghError: GHError): GitHubError = RequestFailure(ghError)

  def fileHandling(forWhat: => String)(fileError: FileError): GitHubError = FileHandling(forWhat, fileError)

  def branchNotFound(gitHubRepo: GitHubRepo, branch: Branch): GitHubError = BranchNotFound(gitHubRepo, branch)

  def pagePublishBranchNotExist(gitHubRepo: GitHubRepo, branch: GitHubPagesBranch): GitHubError =
    PagePublishBranchNotExist(gitHubRepo, branch)

  def render(gitHubError: GitHubError): String = gitHubError match {
    case NonFatalThrowable(message, throwable) =>
      s"GitHubError: $message - ${StackTraceToString.render(throwable)}"

    case MessageOnly(message) =>
      s"GitHubError: $message"

    case RelativePathNotFound(file) =>
      s"GitHubError: Relative path for the file ${file.toString} is not found."

    case RequestFailure(ghError) =>
      s"GitHubError: Access GitHub API has failed - $ghError"

    case FileHandling(forWhat, fileError) =>
      s"GitHubError: FileError when $forWhat - FileError: ${FileError.render(fileError)}"

    case BranchNotFound(gitHubRepo, branch) =>
      s"GitHubError: The branch '${branch.branch}' is not found in the GitHub repo at ${gitHubRepo.org.org}/${gitHubRepo.repo.repo}."

    case PagePublishBranchNotExist(gitHubRepo, branch) =>
      s"""
         |GitHubError: The branch '${AnsiColor.GREEN}${branch.gitHubPagesBranch}${AnsiColor.RESET}' for publishing GitHub pages ${AnsiColor.GREEN}does not exist${AnsiColor.RESET} at ${gitHubRepo
        .org
        .org}/${gitHubRepo.repo.repo}.
         |You need to create it before publishing the doc site.
         |To create it, please ${AnsiColor.BLUE}visit the following link${AnsiColor.RESET} and read sbt-github-pages document.
         |${AnsiColor.BLUE}https://sbt-github-pages.kevinly.dev/docs/run#create-gh-pages-branch${AnsiColor.RESET}
         |""".stripMargin
  }

}
