package org.hammerlab.io.print

/**
 * Wrapper and syntax around [[LinesPrinter]] for easy, declarative defintion of [[ToLines]] instances.
 *
 * Example (adapted from `PrintTest`):
 *
 * {{{
 * case class A(n: Int, s: String)
 *
 * implicit val toLines: ToLines[A] =
 *   new Print(_: A) {
 *     val A(n, s) = t
 *     write(
 *       s"n: $n",
 *       indent { s"a: $a" }
 *     )
 *   }
 * }}}
 *
 * This will create an automatic conversion from an `A` instance to a [[Lines]] with two [[Line]]s, one for each field
 * of `A` (the second one indented).
 *
 * This is a convenient way to define more elaborate, multi-line "toString"-like representations of classes, possibly
 * including levels of indentation and nested within other classes' similar representations.
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
