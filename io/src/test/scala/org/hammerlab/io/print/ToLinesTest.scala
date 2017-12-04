package org.hammerlab.io.print

import hammerlab.print._
import hammerlab.show._
import org.hammerlab.io.print.ToLinesTest._
import org.hammerlab.test.Suite

class ToLinesTest
  extends Suite {
  test("nested indents") {
    import hammerlab.indent.implicits.tab
    Foos(
      111,
      Foos(
        Foos(222),
        333,
        444
      )
    )
    .showLines should be(
      """111
        |		222
        |	333
        |	444"""
        .stripMargin
    )
  }

  test("generic") {
    implicit val indent = hammerlab.indent.spaces.four
    Pair(111, "aaa").showLines should be(
      """Pair(
        |    111
        |    aaa
        |)"""
        .stripMargin
    )
  }

  test("simple") {
    import hammerlab.indent.implicits.spaces.two
    A(123, "abc").showLines should be(
      """123, abc
        |  246
        |  cba"""
        .stripMargin
    )
  }
}

object ToLinesTest {

  /**
   * Example coproduct that wraps an [[Int]] ([[Num]]) or a recursive collection of [[Foo]]s ([[Foos]])
   *
   * Used to demonstrate a [[ToLines]] implementation with progressive levels of indentation; see [[Foos.toLines]]
   */
  sealed trait Foo
  object Foo {
    implicit def makeNum(n: Int): Num = Num(n)
  }

  case class Num(n: Int) extends Foo
  object Num {
    implicit val showNum: Show[Num] = Show { _.n.show }
  }

  case class Foos(foos: Foo*) extends Foo
  object Foos {
    implicit val toLines: ToLines[Foos] =
      ToLines[Foos] {
        case Foos(foos @ _*) ⇒
          foos map {
            case foos: Foos ⇒ toLines(foos).indent
            case Num(n) ⇒ Lines(n.show)
          }
      }
  }

  case class Pair(n: Int, s: String)

  /**
   * Example class with an "inline" [[ToLines]] instance
   */
  case class A(n: Int, s: String)

  object A {
    implicit val toLines: ToLines[A] =
      (a: A) ⇒ {
        val A(n, s) = a
        Lines(
          s"$n, $s",
          indent(
            n * 2,
            s.reverse
          )
        )
      }
  }
}
