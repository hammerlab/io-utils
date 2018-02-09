package org.hammerlab.exception

import hammerlab.indent._
import hammerlab.lines.ToLines
import hammerlab.print._
import hammerlab.show._
import org.hammerlab.io.lines.{ Line, Lines }

/**
 * Wrapper for a [[Throwable]] mimicking standard [[toString]], but with configurable indentation and [[cats.Show]]
 * integration
 */
case class Error(t: Throwable)

object Error {
//  implicit def show(implicit indent: Indent = tab): Show[Error] =
//    Show[Error] {
//      toLines(_).show
//    }

  implicit val toLines: ToLines[Error] =
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
