package org.hammerlab.io

import java.io.{ ByteArrayOutputStream, PrintStream }

/**
 * Wrapper for implementing [[cats.Show]]s using a [[Printer]].
 *
 * Example syntax (from test):
 *
 * {{{
 * implicit val showA =
 *     Print[A] {
 *       new Print(_) {
 *         val A(n, s) = t
 *         echo(s"$n, $s")
 *       }
 *     }
 * }}}
 *
 * [[CanPrint.echo echo]] and other [[CanPrint]] utilities are availabe to write to an implicit [[Printer]] wrapping a
 * [[ByteArrayOutputStream]] from which a string-representation is parsed.
 */
abstract class Print[T](val t: T)(
    implicit val indent: Indent
) extends CanPrint
    with Serializable {
  val bytes = new ByteArrayOutputStream()
  implicit val stringPrinter = Printer(new PrintStream(bytes))

  lazy val shown = bytes.toString()
}

object Print {
  def apply[T](fn: T ⇒ Print[T]): cats.Show[T] = cats.Show.show[T](fn(_).shown)
}
