package org.hammerlab.exception

import hammerlab.lines._
import hammerlab.show._

case class StackTrace(elements: Seq[StackTraceElem])
object StackTrace {
  implicit def apply(e: Throwable): StackTrace =
    StackTrace(
      e
        .getStackTrace
        .map {
          e ⇒ e: StackTraceElem
        }
    )

  implicit val lines: ToLines[StackTrace] =
    ToLines { _.elements.lines }
}
