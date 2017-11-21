package org.hammerlab.io

import hammerlab.show._
import org.hammerlab.test.Suite

/**
 * Example class with using [[Print]] syntax to construct a [[Show]] using a [[Printer]]
 */
case class A(n: Int, s: String)

object A {
  import hammerlab.indent.tab
  implicit val showA =
    Print[A] {
      new Print(_) {
        /** Unpack argument; implicit [[Printer]] is in scope */
        val A(n, s) = t

        /** Contrived representation exercising a couple [[CanPrint]] syntaxes */
        echo(s"$n, $s")
        print(
          n * 2,
          s.reverse
        )
      }
    }
}

class PrintTest
  extends Suite {
  test("A") {
    A(123, "abc").show should be(
      """123, abc
        |246
        |cba
        |""".stripMargin
    )
  }
}
