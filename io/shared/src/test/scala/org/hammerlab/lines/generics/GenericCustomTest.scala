package org.hammerlab.lines.generics

import hammerlab.Suite
import hammerlab.indent.spaces
import hammerlab.lines._
import hammerlab.show._

class GenericCustomTest
  extends Suite {

  val l = Child1("abc", "def")
  val custom =
    """abc
      |def""".stripMargin

  val generic =
    """Child1(
      |  Seq(
      |    abc,
      |    def
      |  )
      |)"""
      .stripMargin

  test("custom lines from companion vs auto-derived") {
    {
      ==((l: Trait).showLines, custom)

      {
        import hammerlab.lines.generic._

        /**
         * with full generic derivations in scope, [[caseclass]] takes precedence over
         * [[Child1.lines types' companions]]
         */
        ===( l        .showLines, generic)
        ===((l: Trait).showLines, generic)
      }

      {
        import hammerlab.lines.generic._
        import Child1.lines  // overrides auto-derived ToLines from generic above

        ===(l.showLines, custom)
      }
    }
  }
}

sealed trait Trait

case class Child1(lines: String*) extends Trait
object Child1 {
  implicit val lines: ToLines[Child1] =
    ToLines {
      case Child1(lines @ _*) ⇒
        lines.map(Lines(_))
    }
}

case class Child2(n: Int) extends Trait
object Child2 {
  implicit val lines: ToLines[Child2] = ???
}
