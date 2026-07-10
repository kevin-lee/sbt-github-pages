package filef

import hedgehog.*
import hedgehog.runner.*

import java.io.{File, FileNotFoundException, IOException}

/** @author Kevin Lee
  * @since 2026-07-10
  */
object FileErrorSpec extends Properties {

  override def tests: List[Test] = List(
    example("FileError.fromNonFatal maps FileNotFoundException to FileNotFound", testFromNonFatalFileNotFound),
    example("FileError.fromNonFatal maps IOException to Io", testFromNonFatalIo),
    example("FileError.fromNonFatal maps any other non-fatal Throwable to Unknown", testFromNonFatalUnknown),
    example("FileError.render(FileNotFound) reports no file found", testRenderFileNotFound),
    example("FileError.render(Io) reports an IOException", testRenderIo),
    example("FileError.render(Unknown) reports an unknown NonFatal Throwable", testRenderUnknown),
    example("FileError.render(NotDirectory) names the file", testRenderNotDirectory),
    example("FileError.render(NotInBaseDir) names the file and the baseDir", testRenderNotInBaseDir),
  )

  def testFromNonFatalFileNotFound: Result = {
    val throwable = new FileNotFoundException("no-such-file")
    FileError.fromNonFatal.lift(throwable).matchPattern { case Some(FileError.FileNotFound(`throwable`)) => }
  }

  def testFromNonFatalIo: Result = {
    val throwable = new IOException("broken pipe")
    FileError.fromNonFatal.lift(throwable).matchPattern { case Some(FileError.Io(`throwable`)) => }
  }

  def testFromNonFatalUnknown: Result = {
    val throwable = new RuntimeException("something else")
    FileError.fromNonFatal.lift(throwable).matchPattern { case Some(FileError.Unknown(`throwable`)) => }
  }

  def testRenderFileNotFound: Result = {
    val rendered = FileError.render(FileError.fileNotFound(new FileNotFoundException("no-such-file")))
    Result
      .assert(rendered.contains("No file found"))
      .and(Result.assert(rendered.contains("no-such-file")))
      .log(s"rendered=$rendered")
  }

  def testRenderIo: Result = {
    val rendered = FileError.render(FileError.io(new IOException("broken pipe")))
    Result
      .assert(rendered.contains("IOException"))
      .and(Result.assert(rendered.contains("broken pipe")))
      .log(s"rendered=$rendered")
  }

  def testRenderUnknown: Result = {
    val rendered = FileError.render(FileError.unknown(new RuntimeException("something else")))
    Result
      .assert(rendered.contains("Unknown NonFatal Throwable"))
      .and(Result.assert(rendered.contains("something else")))
      .log(s"rendered=$rendered")
  }

  def testRenderNotDirectory: Result = {
    val file     = new File("some-file.txt")
    val rendered = FileError.render(FileError.notDirectory(file))
    Result
      .assert(rendered.contains("FileError.NotDirectory"))
      .and(Result.assert(rendered.contains("some-file.txt")))
      .log(s"rendered=$rendered")
  }

  def testRenderNotInBaseDir: Result = {
    val baseDir  = new File("base-dir")
    val file     = new File("elsewhere/some-file.txt")
    val rendered = FileError.render(FileError.notInBaseDir(baseDir, file))
    Result
      .assert(rendered.contains("base-dir"))
      .and(Result.assert(rendered.contains("some-file.txt")))
      .log(s"rendered=$rendered")
  }

}
