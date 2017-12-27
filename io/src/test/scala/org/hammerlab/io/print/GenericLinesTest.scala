package org.hammerlab.io.print

import hammerlab.print._
import hammerlab.print.generic._
import hammerlab.show._
import org.hammerlab.io.print.GenericLinesTest._
import org.hammerlab.test.Suite

class GenericLinesTest
  extends Suite {

  test("generic") {
    implicit val indent = hammerlab.indent.spaces.four
    Pair(111, "aaa").showLines should be(
      """Pair(
        |    111,
        |    aaa
        |)"""
        .stripMargin
    )
  }

  implicit val showDouble: Show[Double] = Show { "%.1f".format(_) }

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
        |      Vector(
        |        111.0,
        |        222.0
        |      ),
        |      333.0
        |    ),
        |    B(
        |      Vector(
        |        444.0
        |      ),
        |      555.0
        |    )
        |  )
        |)"""
        .stripMargin
    )
  }

  test("custom names") {
    import hammerlab.indent.implicits.spaces.two
    implicit def seqName[T]: Name[Seq[T]] = "seq"
    Seq(1, 2).showLines should be(
      """seq(
        |  1,
        |  2
        |)"""
        .stripMargin
    )

    implicit def arrName[T]: Name[Array[T]] = "arr"
    Array(1, 2).showLines should be(
      """arr(
        |  1,
        |  2
        |)"""
        .stripMargin
    )
  }
}

object GenericLinesTest {
  case class Pair(n: Int, s: String)

  case class B(values: Vector[Double], d: Double)
  case class C(bs: Seq[B])
}
