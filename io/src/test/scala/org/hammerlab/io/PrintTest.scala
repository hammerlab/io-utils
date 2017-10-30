package org.hammerlab.io

import hammerlab.show._
import org.hammerlab.test.Suite

/**
 * Example class with using [[Print]] syntax to construct a [[Show]] using a [[Printer]]
 */
case class A(n: Int, s: String)

object A {
  implicit val showA =
    Print[A] {
      new Print(_) {
        val A(n, s) = t
        echo(s"$n, $s")
      }
    }
}

class PrintTest
  extends Suite {
  test("A") {
    A(111, "aaa").show should be("111, aaa\n")
  }
}
