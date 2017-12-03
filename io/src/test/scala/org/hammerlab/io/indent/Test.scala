package org.hammerlab.io.indent

import hammerlab.show._
import org.hammerlab.io.indent.Test._
import org.hammerlab.io.print.Lines
import org.hammerlab.test.Suite

class Test
  extends Suite {
  test("tab") {
    import hammerlab.indent.implicits.tab
    (Foos(
      111,
      Foos(
        Foos(222),
        333,
        444
      )
    ): Lines)
    .show should be(
      """111
        |		222
        |	333
        |	444"""
        .stripMargin
    )
  }

  test("syntax") {
//    val indent = tab
//    indent.show should be("")
//
//    // increment preserves type
//    val tab1: tabs = +indent
//
//    tab1.show should be("\t")
//    indent.show should be("\t")
//
//    +indent
//    indent.show should be("\t\t")
//
//    -indent
//    indent.show.should(be("\t"))
  }
}

object Test {

  /**
   * Example coproduct that wraps an [[Int]] ([[Num]]) or a recursive collection of [[Foo]]s ([[Foos]])
   *
   * Used to demonstrate a [[cats.Show]] implementation with progressive levels of indentation; see [[Foos.showFoos]]
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
    implicit val showFoos: ToLines[Foos] =
      ToLines[Foos] {
        case Foos(foos @ _*) ⇒
          foos map {
            case foos: Foos ⇒ showFoos.apply(foos).indent
            case Num(n) ⇒ Lines(n.show)
          }
      }
  }
}
