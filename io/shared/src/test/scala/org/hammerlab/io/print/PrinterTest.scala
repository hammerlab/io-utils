package org.hammerlab.io.print

import hammerlab.lines._
import hammerlab.show._
import org.hammerlab.Suite

trait PrinterTest
  extends Suite
    with CanPrint {

  implicit val tab = hammerlab.indent.implicits.tab

  trait TestPrinter {
    def printer: Printer
    def read: String
  }

  def printer: TestPrinter

//  implicit def unwrapTestPrinter(p: TestPrinter): Printer = p.printer

  def check(implicit
            printLimit: Limit,
            expected: String): Unit = {
    implicit val printer = this.printer
    implicit val _p = printer.printer

    val list = 1 to 10

    print(
      list,
      "Integers:",
      n ⇒ s"First $n of ${list.size} integers:"
    )

    printer.read should be(expected.stripMargin)
  }

  test("untruncated list") {
    check(
      Limit(None),
      """Integers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	6
        |	7
        |	8
        |	9
        |	10
        |"""
    )
  }

  test("barely not truncated") {
    check(
      Limit(9),
      """Integers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	6
        |	7
        |	8
        |	9
        |	10
        |"""
    )
  }

  test("barely truncated") {
    check(
      Limit(8),
      """First 8 of 10 integers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	6
        |	7
        |	8
        |	…
        |"""
    )
  }

  test("half truncated") {
    check(
      Limit(5),
      """First 5 of 10 integers:
        |	1
        |	2
        |	3
        |	4
        |	5
        |	…
        |"""
    )
  }

  test("one") {
    check(
      Limit(1),
      """First 1 of 10 integers:
        |	1
        |	…
        |"""
    )
  }

  test("none") {
    check(
      Limit(0),
      ""
    )
  }

  test("indent blocks") {
    implicit val printer = this.printer
    implicit val _p: Printer = printer.printer

    print(
      "aaa",
      "bbb",
      indent(
        "ccc",
        indent(
          indent("ddd"),
          ""
        ),
        "eee"
      ),
      "fff"
    )

    indent {
      echo(
        indent(
          "ggg",
          indent(
            "hhh"
          )
        )
      )
      echo(
        indent {
          "iii"
        }
      )
    }

    printer.read should be(
      """aaa
        |bbb
        |	ccc
        |			ddd
        |		
        |	eee
        |fff
        |		ggg
        |			hhh
        |		iii
        |"""
        .stripMargin
    )
  }
}
