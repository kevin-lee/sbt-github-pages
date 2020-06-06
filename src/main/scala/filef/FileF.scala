package filef

import java.io.{ByteArrayOutputStream, File, FileInputStream}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import cats._
import cats.data.EitherT
import cats.effect._
import cats.implicits._
import effectie.Effectful._
import effectie.cats.EffectConstructor

import scala.annotation.tailrec

/**
 * @author Kevin Lee
 * @since 2020-05-27
 */
object FileF {

  final case class BufferSize(bufferSize: Int) extends AnyVal

  def getAllFiles[F[_]: EffectConstructor: Monad](
    dirs: Vector[File]
  ): F[Either[FileError, Vector[File]]] =
    getFiles(dirs, _ => true)

  def getFiles[F[_]: EffectConstructor: Monad](
    dirs: Vector[File],
    fileFilter: File => Boolean
  ): F[Either[FileError, Vector[File]]] =
    effectOfPure(dirs)
      .map(dirs =>
        (for {
          dir <- dirs
          file <- dir.listFiles(fl => fl.isFile && fileFilter(fl))
        } yield file).asRight[FileError]
      )

  def getAllDirsRecursively[F[_]: EffectConstructor: Monad](
    rootDir: File,
    dirFilter: File => Boolean
  ): F[Either[FileError, Vector[File]]] = {
    //TODO: improve it with proper error handling
    @tailrec
    def getAllSubDirs(dirs: Vector[File], dirFilter: File => Boolean, acc: Vector[File]): Vector[File] =
      dirs match {
        case Vector() =>
          acc
        case dir +: restDirs =>
          if (dir.isDirectory && dirFilter(dir) && !acc.exists(_.getCanonicalPath === dir.getCanonicalPath))
            getAllSubDirs(restDirs ++ dir.listFiles().toVector, dirFilter, acc :+ dir)
          else
            getAllSubDirs(restDirs, dirFilter, acc)
      }

    effectOf(getAllSubDirs(Vector(rootDir), dirFilter, Vector.empty).asRight[FileError])
  }

  def readBytesFromFile[F[_]: EffectConstructor: Monad: Sync](
    file: File,
    bufferSize: BufferSize
  ): F[Either[FileError, Array[Byte]]] = {

    def readFile0(
      fileChannel: FileChannel,
      bufferSize: Int,
      byteArrayConsumer: ByteArrayOutputStream
    ): Unit = {
      val buffer = new Array[Byte](bufferSize)
      val byteBuffer = ByteBuffer.wrap(buffer)
      var bytesRead = fileChannel.read(byteBuffer)
      while (-1 != bytesRead) {
        byteArrayConsumer.write(buffer, 0, bytesRead)
        byteBuffer.clear()
        bytesRead = fileChannel.read(byteBuffer)
      }
    }

    val attempted = Resource.make(
      effectOf(new FileInputStream(file))
        .flatMap(fileInputStream =>
          effectOf((fileInputStream, fileInputStream.getChannel()))
        )
    ) { case (fileInputStream, fileChannel) =>
      effectOf(fileChannel.close()) *> effectOf(fileInputStream.close())
    }.use { case (_, fileChannel) =>
      for {
        bytes <- effectOfPure(new ByteArrayOutputStream(bufferSize.bufferSize))
        _ <- effectOf(readFile0(fileChannel, bufferSize.bufferSize, bytes))
      } yield bytes.toByteArray
    }.attempt
    EitherT(attempted).leftMap(FileError.fromNonFatal).value
  }
}
