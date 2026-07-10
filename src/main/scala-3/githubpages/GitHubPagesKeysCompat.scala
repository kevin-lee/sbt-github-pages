package githubpages

import scala.language.implicitConversions

/** The Scala 3 half of the `durationInt` conversion exposed through `autoImport`.
  *
  * Scala 3 only applies `Conversion` instances implicitly, so the scala-2 twin's implicit function
  * value would compile here but silently do nothing, breaking
  * `gitHubPagesPublishRequestTimeout := 3.minutes` for sbt 2 users.
  *
  * @author Kevin Lee
  * @since 2026-07-10
  */
trait GitHubPagesKeysCompat {

  implicit val durationInt: Conversion[Int, scala.concurrent.duration.DurationInt] =
    (n: Int) => new scala.concurrent.duration.DurationInt(n)

}
