package org.hammerlab.io

import hammerlab.indent.ToLines
import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.io.print.Lines._
import org.hammerlab.io.print.{ LinesPrinter, Print, Printer }
import org.hammerlab.test.Suite

class PrintTest
  extends Suite {
  test("A") {
    A(123, "abc").showLines should be(
      """123, abc
        |  246
        |  cba"""
        .stripMargin
    )
  }
}

/**
 * Example class with using [[Print]] syntax to construct a [[ToLines]] using a [[LinesPrinter]]
 */
case class A(n: Int, s: String)

object A {
  implicit val toLines: ToLines[A] =
    new Print(_: A) {
      // Unpack argument
      val A(n, s) = t

      /** Contrived representation exercising a couple [[Printer]] syntaxes */
      write(
        s"$n, $s",
        indent(
          n * 2,
          s.reverse
        )
      )
    }
}
