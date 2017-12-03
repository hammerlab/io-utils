package org.hammerlab.exception

import org.hammerlab.io.print.{ Line, Lines }

case class StackTrace(elements: Seq[StackTraceElem]) {
  def lines: Lines =
    elements
      .map(
        e ⇒ Line(e.toString)
      )
}

object StackTrace {
  implicit def apply(e: Throwable): StackTrace =
    StackTrace(
      e
        .getStackTrace
        .map {
          e ⇒ e: StackTraceElem
        }
    )
}
