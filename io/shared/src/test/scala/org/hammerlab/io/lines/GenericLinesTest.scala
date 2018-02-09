package org.hammerlab.io.lines

import hammerlab.lines._
import hammerlab.lines.generic._
import hammerlab.show._
import org.hammerlab.Suite
import org.hammerlab.io.lines.GenericLinesTest._

class GenericLinesTest
  extends Suite {

  test("generic") {
    import hammerlab.indent.implicits.spaces.four
    Pair(111, "aaa").showLines should be(
      """Pair(
        |    111,
        |    aaa
        |)"""
        .stripMargin
    )
  }

  implicit val showDouble: Show[Double] = Show { "%.1f".format(_) }

  import hammerlab.indent.implicits.spaces.two

  test("nested case classes and seqs") {
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
