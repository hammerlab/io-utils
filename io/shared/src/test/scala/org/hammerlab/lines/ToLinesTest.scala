package org.hammerlab.lines

import java.io.{ ByteArrayOutputStream, PrintStream }

import hammerlab.lines._
import hammerlab.show._
import org.hammerlab.Suite
import org.hammerlab.lines.ToLinesTest._

class ToLinesTest
  extends Suite {
  test("nested indents") {
    import hammerlab.indent.tab
    ==(
      Foos(
        111,
        Foos(
          Foos(222),
          333,
          444
        )
      )
      .showLines,
      """111
        |		222
        |	333
        |	444"""
        .stripMargin
    )
  }

  {
    import hammerlab.indent.spaces

    test("simple") {
      val a = A(123, "abc")
      val expected =
        """123, abc
          |  246
          |  cba"""
          .stripMargin

      ==(a.showLines, expected)
      ==(Some(a).showLines, expected)
      ==((Some(a): Option[A]).showLines, expected)
      ==((None: Option[A]).showLines, "")
      ==(a.lines.showLines, expected)
      ==(a.lines.show, expected)

      ==(
        Lines.Indent(2, a.lines).show,
        """    123, abc
          |      246
          |      cba"""
          .stripMargin
      )
    }

    test("print") {
      val baos = new ByteArrayOutputStream()
      implicit val ps = new PrintStream(baos)
      printlns(
        true,
        Some(Seq("abc", "def")),
        Lines("aaa")
      )
      ps.close()
      ==(
        new String(baos.toByteArray),
        """true
          |abc
          |def
          |aaa
          |""".stripMargin
      )
    }

    test("indents") {

      ==(
        Lines(
          Some(
            Lines(
              "ddd",
              "eee",
              "fff"
            )
          )
          .map((lines: Lines) ⇒ indent(lines)): Option[Lines]
        )
        .showLines,
        """  ddd
          |  eee
          |  fff"""
          .stripMargin
      )

      ==(
        Lines(
          "aaa",
          indent(
            Lines(
              Seq(
                "bbb",
                "ccc"
              )
            ),
            Some(
              Lines(
                "ddd",
                "eee",
                "fff"
              )
            )
            .map { indent(_) }
          )
        )
        .showLines,
        """aaa
          |  bbb
          |  ccc
          |    ddd
          |    eee
          |    fff"""
          .stripMargin
      )

      ==(
        Lines(
          "A: {",
          indent(
            "aaa",
            "bbb",
            "ccc: {",
            indent(
              "ddd",
              "eee"
            ),
            "}"
          ),
          "}"
        )
        .showLines,
        """A: {
          |  aaa
          |  bbb
          |  ccc: {
          |    ddd
          |    eee
          |  }
          |}"""
          .stripMargin
      )
    }

    test("append") {
      ==(indent("abc").append(",").showLines, "  abc,")
      ==(Lines(Seq[String]()).append(",").showLines, "")
    }
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
            case foos: Foos ⇒ indent(foos)
            case Num(n) ⇒ Lines(n.show)
          }
      }
  }

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
