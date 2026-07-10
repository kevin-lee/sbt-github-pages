package errors

import hedgehog.*
import hedgehog.runner.*

/** @author Kevin Lee
  * @since 2026-07-10
  */
object StackTraceToStringSpec extends Properties {

  override def tests: List[Test] = List(
    property("StackTraceToString.render contains the throwable's class name and message", testRender)
  )

  def testRender: Property =
    for {
      message <- Gen.string(Gen.alphaNum, Range.linear(1, 20)).log("message")
    } yield {
      val rendered = StackTraceToString.render(new RuntimeException(message))
      Result
        .assert(rendered.contains("java.lang.RuntimeException"))
        .log(s"The rendered stack trace has no class name. rendered=$rendered")
        .and(
          Result
            .assert(rendered.contains(message))
            .log(s"The rendered stack trace has no message. rendered=$rendered")
        )
    }

}
