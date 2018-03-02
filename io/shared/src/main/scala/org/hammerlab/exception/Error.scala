package org.hammerlab.exception

import hammerlab.lines._
import hammerlab.show._

/**
 * Wrapper for a [[Throwable]] mimicking standard [[toString]], but with configurable indentation and [[cats.Show]]
 * integration
 */
case class Error(t: Throwable)

object Error {
  implicit val toLines: ToLines[Error] =
    ToLines {
      case Error(t) ⇒
        Lines(
          t
            .toString
            .split("\n"),
          indent(
            StackTrace(t)
          ),
          Option(t.getCause)
            .map {
              cause ⇒
                indent(
                  Lines(
                    "Caused by:",
                    Error(cause)
                  )
                )
            }
            .getOrElse(Lines())
        )
    }
}
