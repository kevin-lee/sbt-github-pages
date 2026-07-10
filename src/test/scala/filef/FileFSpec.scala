package filef

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import effectie.instances.ce3.fx.ioFx
import hedgehog.*
import hedgehog.runner.*
// `import hedgehog.*` brings `hedgehog.sbt` into scope, which shadows the root `sbt` package.
import _root_.sbt.io.IO as SbtIo

import java.io.File
import java.nio.charset.StandardCharsets

/** @author Kevin Lee
  * @since 2026-07-10
  */
object FileFSpec extends Properties {

  override def tests: List[Test] = List(
    example("FileF.relativePathOf returns the path of a file relative to the baseDir", testRelativePathOf),
    example("FileF.relativePathOf fails with NotInBaseDir when the file is outside the baseDir", testNotInBaseDir),
    example("FileF.relativePathOf fails with NotDirectory when the baseDir is not a directory", testNotDirectory),
    example("FileF.getAllFiles returns every file in the given dirs and no dir", testGetAllFiles),
    example("FileF.getFiles honours the given fileFilter", testGetFiles),
    example("FileF.getAllDirsRecursively returns the root dir and every sub dir", testGetAllDirsRecursively),
    example("FileF.getAllDirsRecursively honours the given dirFilter", testGetAllDirsRecursivelyWithFilter),
    property("FileF.readBytesFromFile reads back exactly the bytes written", testReadBytesFromFile),
  )

  private def canonicalPaths(files: Vector[File]): Set[String] = files.map(_.getCanonicalPath).toSet

  def testRelativePathOf: Result =
    SbtIo.withTemporaryDirectory { baseDir =>
      val subDir = new File(baseDir, "sub")
      SbtIo.createDirectory(subDir)
      val file   = new File(subDir, "some-file.txt")
      SbtIo.write(file, "content")

      FileF.relativePathOf[IO](baseDir, file).unsafeRunSync() ====
        Right(s"sub${File.separator}some-file.txt")
    }

  def testNotInBaseDir: Result =
    SbtIo.withTemporaryDirectory { baseDir =>
      SbtIo.withTemporaryDirectory { otherDir =>
        val file = new File(otherDir, "some-file.txt")
        SbtIo.write(file, "content")

        FileF.relativePathOf[IO](baseDir, file).unsafeRunSync().matchPattern {
          case Left(FileError.NotInBaseDir(_, _)) =>
        }
      }
    }

  def testNotDirectory: Result =
    SbtIo.withTemporaryDirectory { dir =>
      val notADir = new File(dir, "not-a-dir.txt")
      SbtIo.write(notADir, "content")

      FileF.relativePathOf[IO](notADir, notADir).unsafeRunSync().matchPattern {
        case Left(FileError.NotDirectory(_)) =>
      }
    }

  def testGetAllFiles: Result =
    SbtIo.withTemporaryDirectory { dir =>
      val subDir = new File(dir, "sub")
      SbtIo.createDirectory(subDir)
      val file1  = new File(dir, "one.txt")
      val file2  = new File(dir, "two.md")
      SbtIo.write(file1, "1")
      SbtIo.write(file2, "2")

      FileF.getAllFiles[IO](Vector(dir)).unsafeRunSync() match {
        case Right(files) =>
          canonicalPaths(files) ==== canonicalPaths(Vector(file1, file2))

        case Left(fileError) =>
          Result.failure.log(s"Expected the files but got ${FileError.render(fileError)}")
      }
    }

  def testGetFiles: Result =
    SbtIo.withTemporaryDirectory { dir =>
      val markdown = new File(dir, "one.md")
      val text     = new File(dir, "two.txt")
      SbtIo.write(markdown, "1")
      SbtIo.write(text, "2")

      FileF.getFiles[IO](Vector(dir), _.getName.endsWith(".md")).unsafeRunSync() match {
        case Right(files) =>
          canonicalPaths(files) ==== canonicalPaths(Vector(markdown))

        case Left(fileError) =>
          Result.failure.log(s"Expected the files but got ${FileError.render(fileError)}")
      }
    }

  def testGetAllDirsRecursively: Result =
    SbtIo.withTemporaryDirectory { dir =>
      val subDir    = new File(dir, "sub")
      val subSubDir = new File(subDir, "sub-sub")
      SbtIo.createDirectory(subDir)
      SbtIo.createDirectory(subSubDir)
      SbtIo.write(new File(subDir, "one.txt"), "1")

      FileF.getAllDirsRecursively[IO](dir, _ => true).unsafeRunSync() match {
        case Right(dirs) =>
          canonicalPaths(dirs) ==== canonicalPaths(Vector(dir, subDir, subSubDir))

        case Left(fileError) =>
          Result.failure.log(s"Expected the dirs but got ${FileError.render(fileError)}")
      }
    }

  def testGetAllDirsRecursivelyWithFilter: Result =
    SbtIo.withTemporaryDirectory { dir =>
      val kept    = new File(dir, "kept")
      val ignored = new File(dir, "target")
      SbtIo.createDirectory(kept)
      SbtIo.createDirectory(ignored)
      SbtIo.createDirectory(new File(ignored, "classes"))

      FileF.getAllDirsRecursively[IO](dir, file => !file.getName.endsWith("target")).unsafeRunSync() match {
        case Right(dirs) =>
          canonicalPaths(dirs) ==== canonicalPaths(Vector(dir, kept))

        case Left(fileError) =>
          Result.failure.log(s"Expected the dirs but got ${FileError.render(fileError)}")
      }
    }

  def testReadBytesFromFile: Property =
    for {
      content <- Gen.string(Gen.alphaNum, Range.linear(1, 100)).log("content")
    } yield SbtIo.withTemporaryDirectory { dir =>
      val file = new File(dir, "some-file.txt")
      SbtIo.write(file, content, StandardCharsets.UTF_8)

      FileF.readBytesFromFile[IO](file, FileF.BufferSize(8)).unsafeRunSync() match {
        case Right(bytes) =>
          bytes.toList ==== content.getBytes(StandardCharsets.UTF_8).toList

        case Left(fileError) =>
          Result.failure.log(s"Expected the bytes but got ${FileError.render(fileError)}")
      }
    }

}
