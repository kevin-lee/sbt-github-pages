package filef

import java.io.{FileNotFoundException, IOException}

import scala.util.control.NonFatal

/**
 * @author Kevin Lee
 * @since 2020-06-06
 */
sealed trait FileError

object FileError {
  final case class FileNotFound private (
    fileNotFoundException: FileNotFoundException
  ) extends FileError
  private[this] object FileNotFound extends (FileNotFoundException => FileError)

  final case class Io private (ioException: IOException) extends FileError
  private[this] object Io extends (IOException => FileError)

  final case class Unknown private (throwable: Throwable) extends FileError
  private[this] object Unknown extends (Throwable => Unknown)

  def fromNonFatal(throwable: Throwable): FileError = throwable match {
    case fileNotFoundException: FileNotFoundException =>
      fileNotFound(fileNotFoundException)

    case ioException: IOException =>
      io(ioException)

    case NonFatal(ex) =>
      Unknown(ex)
  }

  def fileNotFound(
    fileNotFoundException: FileNotFoundException
  ): FileError = FileNotFound(fileNotFoundException)

  def io(ioException: IOException): FileError = Io(ioException)

  def unknown(throwable: Throwable): FileError = Unknown(throwable)

}
