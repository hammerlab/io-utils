package org.hammerlab.lines.generics

import hammerlab.Suite
import hammerlab.indent.spaces
import hammerlab.lines._
import hammerlab.show._
import org.hammerlab.lines.Name
import org.hammerlab.lines.generics.DeriveToLinesTest._

class DeriveToLinesTest
  extends Suite {

  import hammerlab.lines.generic._

  test("generic") {
    implicit val spaces = hammerlab.indent.spaces.`4`
    ==(
      Pair(111, "aaa").showLines,
      """Pair(
        |    111,
        |    aaa
        |)"""
        .stripMargin
    )
  }

  test("options") {
    ==(None.showLines, "None")
    ==((None: Option[Int]).showLines, "None")
    ==(Some(2).showLines, "Some(2)")
    ==(Some("abc").showLines, "Some(abc)")
  }

  test("tuples") {
    ==(
      (1, 2).showLines,
      """(
        |  1,
        |  2
        |)"""
      .stripMargin
    )

    ==(
      (1, "abc").showLines,
      """(
        |  1,
        |  abc
        |)"""
        .stripMargin
    )

    ==(
      (1, "abc", true).showLines,
      """(
        |  1,
        |  abc,
        |  true
        |)"""
        .stripMargin
    )
  }

  val c =
    C(
      Seq(
        B(Vector(111, 222), 333),
        B(Vector(444), 555)
      )
    )

  test("nested case classes") {
    ==(
      c.showLines,
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

    ==(D(None).showLines, "D(None)")
    ==(
      D(Some(c)).showLines,
      """D(
        |  Some(
        |    C(
        |      Seq(
        |        B(
        |          Vector(
        |            111.0,
        |            222.0
        |          ),
        |          333.0
        |        ),
        |        B(
        |          Vector(
        |            444.0
        |          ),
        |          555.0
        |        )
        |      )
        |    )
        |  )
        |)"""
        .stripMargin
    )

    ==(E().showLines, "E")
    ==(F.showLines, "F")
  }

  test("sealed trait") {
    ==(
      List(
        B(Vector(777, 888), 999),
        c
      )
      .showLines,
      """List(
        |  B(
        |    Vector(
        |      777.0,
        |      888.0
        |    ),
        |    999.0
        |  ),
        |  C(
        |    Seq(
        |      B(
        |        Vector(
        |          111.0,
        |          222.0
        |        ),
        |        333.0
        |      ),
        |      B(
        |        Vector(
        |          444.0
        |        ),
        |        555.0
        |      )
        |    )
        |  )
        |)""".stripMargin
    )
  }

  test("custom names") {
    implicit def seqName[T]: Name[Seq[T]] = "seq"
    ==(
      Seq(1, 2).showLines,
      """seq(
        |  1,
        |  2
        |)"""
        .stripMargin
    )

    implicit def arrName[T]: Name[Array[T]] = "arr"
    ==(
      Array(1, 2).showLines,
      """arr(
        |  1,
        |  2
        |)"""
        .stripMargin
    )
  }

  test("seqs") {
    ==(Nil.showLines, "Nil")
    ==(Seq[Int]().showLines, "Seq()")
  }
}



object DeriveToLinesTest {

  case class Pair(n: Int, s: String)

  sealed trait A extends Product with Serializable
   case  class B(values: Vector[Double], d: Double) extends A
   case  class C(bs: Seq[B]) extends A
   case  class D(c: Option[C])
   case  class E()
   case object F

  implicit val showDouble: Show[Double] = Show { "%.1f".format(_) }
}
