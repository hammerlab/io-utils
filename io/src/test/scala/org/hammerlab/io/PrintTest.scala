package org.hammerlab.io

import hammerlab.indent.implicits.spaces2
import hammerlab.print._
import hammerlab.show._
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
 * Example class with using [[Print]] syntax to construct a [[ToLines]] using a [[org.hammerlab.io.print.LinesPrinter]]
 */
case class A(n: Int, s: String)

object A {
  implicit val toLines: ToLines[A] =
    new Print(_: A) {
      // Unpack argument
      val A(n, s) = t

      /** Contrived representation exercising a couple [[Printer]] syntaxes */
      write(
        s"$n, $s"
      )
      echo(
        indent(
          n * 2,
          s.reverse
        )
      )
    }
}
