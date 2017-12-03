package org.hammerlab.exception

import hammerlab.indent._
import hammerlab.print._
import hammerlab.show._

/**
 * Wrapper for a [[Throwable]] mimicking standard [[toString]], but with configurable indentation and [[cats.Show]]
 * integration
 */
case class Error(t: Throwable)

object Error {
  implicit def show(implicit indent: Indent = tab): Show[Error] =
    Show[Error] {
      lines(_).show
    }

  implicit val lines: ToLines[Error] =
    ToLines {
      case Error(t) ⇒
        Lines(
          t
            .toString
            .split("\n")
            .map(Line(_))
            .toIterable,
          StackTrace(t).lines.indent,
          Option(t.getCause).map {
            cause ⇒
              Lines(
                "Caused by:",
                Error(cause)
              )
              .indent
          } getOrElse(Lines())
        )
    }
}
