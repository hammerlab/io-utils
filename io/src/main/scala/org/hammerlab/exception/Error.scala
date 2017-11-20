package org.hammerlab.exception

import cats.Show
import hammerlab.indent._

/**
 * Wrapper for a [[Throwable]] mimicking standard [[toString]], but with configurable indentation and [[cats.Show]]
 * integration
 */
case class Error(t: Throwable) {
  def lines(implicit indent: Indent): List[String] =
    s"$indent$t" :: indent {
      StackTrace(t).lines(indent) ++
        Option(t.getCause)
          .toList
          .flatMap {
            cause ⇒
              "Caused by:" ::
                Error(cause).lines(indent)
          }
    }


  override def toString: String =
    lines.mkString("\n")
}

object Error {
  implicit def show(implicit indent: Indent = tab): Show[Error] =
    Show.show {
      _
        .lines(indent)
        .mkString("\n")
    }
}
