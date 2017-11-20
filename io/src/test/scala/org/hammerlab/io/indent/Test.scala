package org.hammerlab.io.indent

import hammerlab.indent.Indent
import hammerlab.show._
import org.hammerlab.io.indent.Test._
import org.hammerlab.io.tabs
import org.hammerlab.test.Suite

class Test
  extends Suite {
  import hammerlab.indent.tab
  test("tab") {
    Foos(
      111,
      Foos(
        Foos(222),
        333,
        444
      )
    )
    .show should be(
      """111
        |		222
        |	333
        |	444""".stripMargin
    )
  }

  test("syntax") {
    val indent = tab
    indent.show should be("")

    // increment preserves type
    val tab1: tabs = +indent

    tab1.show should be("\t")
    indent.show should be("\t")

    +indent
    indent.show should be("\t\t")

    -indent
    indent.show.should(be("\t"))
  }
}

object Test {
  import cats.Show
  import Show.show

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
    implicit val showNum: Show[Num] = show { _.n.show }
  }

  case class Foos(foos: Foo*) extends Foo
  object Foos {
    implicit def showFoos(implicit indent: Indent): Show[Foos] =
      show {
        case Foos(foos @ _*) ⇒
          foos map {
            case foos: Foos ⇒ indent { showFoos.show(foos) }  // Bump indentation level; recurse
            case Num(n) ⇒ show"$indent$n"  // Prepend accumulated indent
          } mkString("\n")
      }
  }
}
