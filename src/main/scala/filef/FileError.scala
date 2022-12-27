package filef

import errors.StackTraceToString

import java.io.{File, FileNotFoundException, IOException}
import scala.util.control.NonFatal

/** @author Kevin Lee
  * @since 2020-06-06
  */
sealed trait FileError

object FileError {

  final case class FileNotFound private (
    fileNotFoundException: FileNotFoundException
  ) extends FileError

  final case class Io private (ioException: IOException) extends FileError

  final case class Unknown private (throwable: Throwable) extends FileError

  final case class NotDirectory(file: File) extends FileError

  final case class NotInBaseDir(baseDir: File, file: File) extends FileError

  def fromNonFatal: PartialFunction[Throwable, FileError] = {
    case fileNotFoundException: FileNotFoundException =>
      FileError.fileNotFound(fileNotFoundException)

    case ioException: IOException =>
      FileError.io(ioException)

    case NonFatal(ex) =>
      FileError.unknown(ex)
  }

  def fileNotFound(
    fileNotFoundException: FileNotFoundException
  ): FileError = FileNotFound(fileNotFoundException)

  def io(ioException: IOException): FileError = Io(ioException)

  def unknown(throwable: Throwable): FileError = Unknown(throwable)

  def notDirectory(file: File): FileError = NotDirectory(file)

  def notInBaseDir(baseDir: File, file: File): FileError = NotInBaseDir(baseDir, file)

  def render(fileError: FileError): String = fileError match {
    case FileNotFound(fileNotFoundException) =>
      s"No file found - Error: ${StackTraceToString.render(fileNotFoundException)}"

    case Io(ioException) =>
      s"IOException - Error: ${StackTraceToString.render(ioException)}"

    case Unknown(throwable) =>
      s"Unknown NonFatal Throwable - Error: ${StackTraceToString.render(throwable)}"

    case NotDirectory(file) =>
      s"FileError.NotDirectory - $file is not a directory."

    case NotInBaseDir(baseDir, file) =>
      s"The given file [$file] is not in the baseDir [$baseDir]"
  }

}
