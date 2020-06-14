package effectie.cats

/**
 * @author Kevin Lee
 * @since 2020-06-07
 */
// TODO: Replace it with Effectie later
trait Attemptable {

  import Attemptable._

  final def attempt[F[_], A]: CurriedAttempt1[F, A] =
    new CurriedAttempt1[F, A]

  final def attemptF[A]: CurriedAttemptF1[A] =
    new CurriedAttemptF1[A]

}

object Attemptable extends Attemptable {

  @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
  private[Attemptable] final class CurriedAttempt1[F[_], A] {
    def apply[B](fb: F[B]): CurriedAttempt2[F, A, B] =
      new CurriedAttempt2[F, A, B](fb)
  }
  private[Attemptable] final class CurriedAttempt2[F[_], A, B](
    private val fb: F[B]
  ) extends AnyVal {
    def apply(f: Throwable => A)(implicit AT: Attempt[F]): F[Either[A, B]] =
      Attempt[F].attempt(fb)(f)
  }

  @SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
  private[Attemptable] final class CurriedAttemptF1[A] {
    def apply[F[_], B](b: => B): CurriedAttemptF2[F, A, B] =
      new CurriedAttemptF2[F, A, B](() => b)
  }

  private[Attemptable] final class CurriedAttemptF2[F[_], A, B](
    private val b: () => B
  ) extends AnyVal {
    def apply(f: Throwable => A)(implicit EC: EffectConstructor[F], AT: Attempt[F]): F[Either[A, B]] =
      Attempt[F].attempt(EffectConstructor[F].effectOf(b()))(f)
  }

}
