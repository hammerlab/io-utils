package org.hammerlab.io

import hammerlab.indent.ToLines
import hammerlab.indent.implicits.spaces2
import hammerlab.show._
import org.hammerlab.io.print.Lines
import org.hammerlab.test.Suite

/**
 * Example class with using [[Print]] syntax to construct a [[Show]] using a [[Printer]]
 */
case class A(n: Int, s: String)

object A {
  implicit val toLines: ToLines[A] =
    LinePrint[A] {
      new LinePrint(_) {
        /** Unpack argument; implicit [[Printer]] is in scope */
        val A(n, s) = t

        /** Contrived representation exercising a couple [[CanPrint]] syntaxes */
        write(
          s"$n, $s",
          indent(
            n * 2,
            s.reverse
          )
        )
      }
    }
}

class PrintTest
  extends Suite {
  test("A") {
    (A(123, "abc"): Lines).show should be(
      """123, abc
        |  246
        |  cba"""
        .stripMargin
    )
  }
}
