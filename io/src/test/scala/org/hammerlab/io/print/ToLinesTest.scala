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

  val dropRightZeros = """^(.*?)0*$""".r
  implicit val showDouble: Show[Double] =
    Show {
      d ⇒
        "%.5f".format(d) match {
          case dropRightZeros(s) ⇒
            if (s.endsWith("."))
              s"${s}0"
            else
              s
        }
    }

  test("nested case classes and seqs") {
    import hammerlab.indent.implicits.spaces.two
    C(
      Seq(
        B(Vector(111, 222), 333),
        B(Vector(444), 555)
      )
    )
    .showLines should be(
      """C(
        |  Seq(
        |    B(
        |      Seq(
        |        111.0
        |        222.0
        |      )
        |      333.0
        |    )
        |    B(
        |      Seq(
        |        444.0
        |      )
        |      555.0
        |    )
        |  )
        |)""".stripMargin
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
      ToLines {
        case Foos(foos @ _*) ⇒
          foos map {
            case foos: Foos ⇒ toLines(foos).indent
            case Num(n) ⇒ Lines(n.show)
          }
      }
  }

  case class Pair(n: Int, s: String)

  case class B(values: Vector[Double], d: Double)
  case class C(bs: Seq[B])

  /**
   * Example class with an "inline" [[ToLines]] instance
   */
  case class A(n: Int, s: String)

  object A {
    implicit val toLines: ToLines[A] =
      ToLines {
        case A(n, s) ⇒
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
