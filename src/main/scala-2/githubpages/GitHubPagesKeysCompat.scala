package githubpages

/** The Scala 2 half of the `durationInt` conversion exposed through `autoImport`.
  *
  * On Scala 2 an implicit value of function type is applied as an implicit conversion, so this is
  * enough to let a build write `gitHubPagesPublishRequestTimeout := 3.minutes`. Scala 3 does not do
  * that, which is why the scala-3 twin of this trait uses `Conversion` instead.
  *
  * @author Kevin Lee
  * @since 2026-07-10
  */
trait GitHubPagesKeysCompat {

  implicit val durationInt: Int => scala.concurrent.duration.DurationInt = scala.concurrent.duration.DurationInt

}
