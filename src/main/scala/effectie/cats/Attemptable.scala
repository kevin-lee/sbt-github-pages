package effectie.cats

/**
 * @author Kevin Lee
 * @since 2020-06-07
 */
trait Attemptable {

  def attempt[F[_]: Attempt, A, B](fb: F[B])(f: Throwable => A): F[Either[A, B]] =
    Attempt[F].attempt(fb)(f)

  def attemptF[F[_]: Attempt: EffectConstructor, A, B](
    b: => B
  )(
    f: Throwable => A
  ): F[Either[A, B]] =
    Attempt[F].attempt(EffectConstructor[F].effectOf(b))(f)

}

object Attemptable extends Attemptable