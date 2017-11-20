package org.hammerlab.exception

import hammerlab.indent.{ Indent, tabs }

case class StackTrace(elements: Seq[StackTraceElem]) {
  def lines(implicit indent: Indent = tabs(1)): List[String] =
    elements
      .toList
      .map(
        elem ⇒
          s"$indent$elem"
      )

  override def toString: String = lines.mkString("\n")
}

object StackTrace {
  def apply(e: Throwable): StackTrace =
    StackTrace(
      e
        .getStackTrace
        .map {
          e ⇒ e: StackTraceElem
        }
    )
}
