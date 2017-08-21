package org.hammerlab.io.show

import org.hammerlab.test.Suite
import cats.syntax.all._

// Simple coproduct hierarchy
sealed trait Foo
case class Bar(n: Int, s: String) extends Foo
case class Baz(b: Boolean) extends Foo
case class Qux() extends Foo
case object ZZZ extends Foo

class GenericShowTest
  extends Suite {

  case class Anon(n: Int, s: String)

  test("show") {
    val bar = Bar(111, "aaa")

    bar.show should be("Bar(111,aaa)")

    val baz = Baz(true)

    baz.show should be("Baz(true)")

    (bar: Foo).show should be("Bar(111,aaa)")

    val qux = Qux()

    qux.show should be("Qux")

    ZZZ.show should be("ZZZ$")

    val foos: Seq[Foo] = List(bar, baz, qux, ZZZ)

    foos.map(_.show).mkString(" ") should be("Bar(111,aaa) Baz(true) Qux ZZZ$")

    Anon(123, "abc").show should be("Anon(123,abc)")
  }
}
