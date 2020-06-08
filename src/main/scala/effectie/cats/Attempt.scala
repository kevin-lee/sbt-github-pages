package effectie.cats

import cats.effect.IO
import cats.implicits._

/**
 * @author Kevin Lee
 * @since 2020-06-07
 */
trait Attempt[F[_]] {
  def attempt[A, B](fb: F[B])(f: Throwable => A): F[Either[A, B]]
}

object Attempt {
  def apply[F[_]: Attempt]: Attempt[F] = implicitly[Attempt[F]]

  implicit val attemptIo: Attempt[IO] = new Attempt[IO] {
    override def attempt[A, B](fb: IO[B])(f: Throwable => A): IO[Either[A, B]] =
      fb.attempt.map(_.leftMap(f))
  }
}