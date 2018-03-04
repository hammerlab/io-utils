package org.hammerlab.show

import cats.instances.boolean.catsStdShowForBoolean
import hammerlab.show.all._
import org.hammerlab.Suite

// Simple coproduct hierarchy
sealed trait Foo
case class Bar(n: Int, s: String) extends Foo
case class Baz(b: Boolean) extends Foo
case class Qux() extends Foo
case object ZZZ extends Foo

class ShowTest
  extends Suite {

  case class Anon(n: Int, s: String)

  test("generic") {
    val bar = Bar(111, "aaa")

    bar.show should be("Bar(111,aaa)")

    val baz = Baz(true)

    baz.show should be("Baz(true)")

    (bar: Foo).show should be("Bar(111,aaa)")

    val qux = Qux()

    qux.show should be("Qux")

    // This is "ZZZ$" on the JVM and "ZZZ" in JS
    val zzz = ZZZ.getClass.getSimpleName

    ZZZ.show should be(zzz)

    val foos: Seq[Foo] = List(bar, baz, qux, ZZZ)

    foos.map(_.show).mkString(" ") should be(s"Bar(111,aaa) Baz(true) Qux $zzz")

    Anon(123, "abc").show should be("Anon(123,abc)")
  }

  test("syntax") {
    /** Show.apply alias for [[cats.Show.show]] */
    showInt(4) should be("4")
  }
}
