package org.hammerlab.io.print

import hammerlab.indent._

/**
 *
 * @param t
 * @param _indent
 * @tparam T
 */
class Print[T](val t: T)(
    implicit override val _indent: Indent
)
  extends LinesPrinter

object Print {
  implicit def apply[T](fn: T ⇒ Print[T]): ToLines[T] =
    ToLines {
      fn(_).lines
    }
}
