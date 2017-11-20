package org.hammerlab.exception

import cats.Show
import hammerlab.indent.{ Indent, tabs }

/**
 * Wrapper for a [[Throwable]] mimicking standard [[toString]], but with configurable indentation and [[cats.Show]]
 * integration
 */
case class Error(t: Throwable) {
  def lines(implicit indent: Indent = tabs(1)): List[String] =
    t.toString() ::
      StackTrace(t).lines(indent) ++
        Option(t.getCause)
          .toList
          .flatMap {
            cause ⇒
              "Caused by:" ::
                Error(cause).lines(indent)
          }


  override def toString: String =
    lines.mkString("\n")
}

object Error {
  implicit val show: Show[Error] =
    Show.show {
      _
        .lines
        .mkString("\n")
    }
}
