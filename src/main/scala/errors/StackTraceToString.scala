package errors

import java.io.{PrintWriter, StringWriter}

object StackTraceToString {

  def render(throwable: Throwable): String = {
    val stringWriter = new StringWriter()
    val out          = new PrintWriter(stringWriter)
    throwable.printStackTrace(out)
    stringWriter.toString
  }

}
