package githubpages.github

import filef.FileError
import github4s.GHError
import hedgehog.*
import hedgehog.runner.*

import java.io.File

/** @author Kevin Lee
  * @since 2026-07-10
  */
object GitHubErrorSpec extends Properties {

  override def tests: List[Test] = List(
    example("GitHubError.render(NonFatalThrowable) reports the message and the stack trace", testRenderNonFatal),
    example("GitHubError.render(MessageOnly) reports the message", testRenderMessageOnly),
    example("GitHubError.render(RelativePathNotFound) names the file", testRenderRelativePathNotFound),
    example("GitHubError.render(RequestFailure) reports the GHError", testRenderRequestFailure),
    example("GitHubError.render(FileHandling) reports the context and the FileError", testRenderFileHandling),
    example("GitHubError.render(BranchNotFound) names the branch and the repo", testRenderBranchNotFound),
    example("GitHubError.render(PagePublishBranchNotExist) names the branch and the repo", testRenderPageBranch),
  )

  private def gitHubRepo: Data.GitHubRepo =
    Data.GitHubRepo(Data.GitHubRepo.Org("kevin-lee"), Data.GitHubRepo.Repo("sbt-github-pages"))

  def testRenderNonFatal: Result = {
    val rendered = GitHubError.render(GitHubError.nonFatalThrowable("boom", new RuntimeException("kaboom")))
    Result
      .assert(rendered.contains("GitHubError: boom"))
      .and(Result.assert(rendered.contains("kaboom")))
      .log(s"rendered=$rendered")
  }

  def testRenderMessageOnly: Result =
    GitHubError.render(GitHubError.messageOnly("nothing to commit")) ==== "GitHubError: nothing to commit"

  def testRenderRelativePathNotFound: Result = {
    val rendered = GitHubError.render(GitHubError.relativePathNotFound(new File("some-file.txt")))
    Result.assert(rendered.contains("some-file.txt")).log(s"rendered=$rendered")
  }

  def testRenderRequestFailure: Result = {
    val rendered = GitHubError.render(GitHubError.requestFailure(GHError.BasicError("bad credentials")))
    Result
      .assert(rendered.contains("Access GitHub API has failed"))
      .and(Result.assert(rendered.contains("bad credentials")))
      .log(s"rendered=$rendered")
  }

  def testRenderFileHandling: Result = {
    val rendered =
      GitHubError.render(GitHubError.fileHandling("reading the site dir")(FileError.notDirectory(new File("a.txt"))))
    Result
      .assert(rendered.contains("reading the site dir"))
      .and(Result.assert(rendered.contains("FileError.NotDirectory")))
      .log(s"rendered=$rendered")
  }

  def testRenderBranchNotFound: Result = {
    val rendered = GitHubError.render(GitHubError.branchNotFound(gitHubRepo, Data.Branch("gh-pages")))
    Result
      .assert(rendered.contains("'gh-pages'"))
      .and(Result.assert(rendered.contains("kevin-lee/sbt-github-pages")))
      .log(s"rendered=$rendered")
  }

  def testRenderPageBranch: Result = {
    val rendered =
      GitHubError.render(GitHubError.pagePublishBranchNotExist(gitHubRepo, Data.GitHubPagesBranch("gh-pages")))
    Result
      .assert(rendered.contains("gh-pages"))
      .and(Result.assert(rendered.contains("kevin-lee/sbt-github-pages")))
      .log(s"rendered=$rendered")
  }

}
