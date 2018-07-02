package org.hammerlab.markdown.table

import hammerlab.indent.spaces
import hammerlab.markdown._
import hammerlab.show._

class Test
  extends hammerlab.Suite {

  // Format [[Double]]s to 2 sig-figs
  import hammerlab.math.sigfigs._
  implicit val sigfigs: SigFigs = 2

  val users =
    Seq(
      User("abc def", 123.4567),
      User("ghi jkl mno", 2.34567)
    )

  test("simple") {
    ===(
      users
        .mdTable()
        .showLines,
      """|| User Name | Balance |
         || --- | --- |
         || abc def | 123 |
         || ghi jkl mno | 2.3 |"""
      .stripMargin
    )
  }

  test("override fields") {
    ===(
      users
        .mdTable('balance → "Balance ($)")
        .showLines,
      """|| User Name | Balance ($) |
         || --- | --- |
         || abc def | 123 |
         || ghi jkl mno | 2.3 |"""
      .stripMargin
    )
  }

  test("invalid override") {
    intercept[InvalidFieldOverrides] {
      users.mdTable('balanc → "Balance…")
    }
  }
}

case class User(userName: String, balance: Double)
