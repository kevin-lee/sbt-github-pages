package githubpages

import hedgehog.*
import hedgehog.runner.*

import scala.util.{Failure, Success, Try}

/** @author Kevin Lee
  * @since 2026-07-10
  */
object GitHubPagesPluginSpec extends Properties {

  override def tests: List[Test] = List(
    property("returnOrThrowMessageOnlyException returns the value of a Right", testReturnRight),
    property("returnOrThrowMessageOnlyException throws a MessageOnlyException for a Left", testThrowForLeft),
  )

  def testReturnRight: Property =
    for {
      value <- Gen.string(Gen.alphaNum, Range.linear(1, 20)).log("value")
    } yield GitHubPagesPlugin.returnOrThrowMessageOnlyException(Right(value): Either[Int, String])(code =>
      s"error: $code"
    ) ==== value

  def testThrowForLeft: Property =
    for {
      code <- Gen.int(Range.linear(1, 100)).log("code")
    } yield Try(
      GitHubPagesPlugin.returnOrThrowMessageOnlyException(Left(code): Either[Int, String])(c => s"error: $c")
    ) match {
      // `import hedgehog.*` brings `hedgehog.sbt` into scope, which shadows the root `sbt` package.
      case Failure(throwable: _root_.sbt.MessageOnlyException) =>
        throwable.getMessage ==== s"error: $code"

      case Failure(throwable) =>
        Result.failure.log(s"Expected a MessageOnlyException but got $throwable")

      case Success(value) =>
        Result.failure.log(s"Expected a MessageOnlyException but got the value $value")
    }

}
