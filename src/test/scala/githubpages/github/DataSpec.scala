package githubpages.github

import github4s.GithubConfig
import github4s.domain.TreeResult
import hedgehog.*
import hedgehog.runner.*

import java.io.File

/** @author Kevin Lee
  * @since 2026-07-10
  */
object DataSpec extends Properties {

  override def tests: List[Test] = List(
    example("Data.IsText is true for an accepted extension and a short enough file", testIsTextTrue),
    example("Data.IsText is false when the extension is not accepted", testIsTextUnacceptedExtension),
    example("Data.IsText is false when the file is not shorter than maxLength", testIsTextTooLong),
    example("Data.IsText ignores the case of the file extension", testIsTextCaseInsensitive),
    example("Data.TreeResultSha.fromTreeResult takes the sha of the TreeResult", testTreeResultSha),
    example("Data.GitHubApiConfig.toGithubConfig(default) is GithubConfig.default", testToGithubConfigDefault),
    example("Data.GitHubApiConfig.toGithubConfig(custom) keeps the custom values", testToGithubConfigCustom),
    example("Data.GitHubRepoWithAuth.AccessToken hides the token in toString", testAccessTokenIsProtected),
  )

  private def blobConfig: Data.BlobConfig = Data.BlobConfig(Set(".md", ".html"), 10)

  def testIsTextTrue: Result =
    Result.assert(Data.IsText(blobConfig)(new File("README.md"), Array.fill[Byte](9)(0)))

  def testIsTextUnacceptedExtension: Result =
    Result.assert(!Data.IsText(blobConfig)(new File("logo.png"), Array.fill[Byte](9)(0)))

  def testIsTextTooLong: Result =
    Result.assert(!Data.IsText(blobConfig)(new File("README.md"), Array.fill[Byte](10)(0)))

  def testIsTextCaseInsensitive: Result =
    Result.assert(Data.IsText(blobConfig)(new File("INDEX.HTML"), Array.fill[Byte](1)(0)))

  def testTreeResultSha: Result =
    Data.TreeResultSha.fromTreeResult(TreeResult("some-sha", "some-url", List.empty, None)) ====
      Data.TreeResultSha("some-sha")

  def testToGithubConfigDefault: Result =
    Data.GitHubApiConfig.toGithubConfig(Data.GitHubApiConfig.default) ==== GithubConfig.default

  def testToGithubConfigCustom: Result = {
    val gitHubApiConfig = Data.GitHubApiConfig(
      baseUrl = Data.GitHubApiConfig.BaseUrl("https://github.example.com/api/v3/"),
      authorizeUrl = Data.GitHubApiConfig.AuthorizeUrl("https://github.example.com/login/oauth/authorize"),
      accessTokenUrl = Data.GitHubApiConfig.AccessTokenUrl("https://github.example.com/login/oauth/access_token"),
      headers = Data.GitHubApiConfig.Headers(Map("user-agent" -> "sbt-github-pages")),
    )
    Data.GitHubApiConfig.toGithubConfig(gitHubApiConfig) ====
      GithubConfig(
        baseUrl = "https://github.example.com/api/v3/",
        authorizeUrl = "https://github.example.com/login/oauth/authorize",
        accessTokenUrl = "https://github.example.com/login/oauth/access_token",
        headers = Map("user-agent" -> "sbt-github-pages"),
      )
  }

  @SuppressWarnings(Array("org.wartremover.warts.ToString"))
  def testAccessTokenIsProtected: Result = {
    val accessToken = Data.GitHubRepoWithAuth.AccessToken("super-secret-token")
    Result
      .assert(!accessToken.toString.contains("super-secret-token"))
      .and(accessToken.toString ==== "*** Protected ***")
  }

}
