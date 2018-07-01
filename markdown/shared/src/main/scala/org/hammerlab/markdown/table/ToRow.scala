package org.hammerlab.markdown.table

import hammerlab.show._
import shapeless._

trait ToRow[T] {
  def apply(t: T): Seq[String]
}

trait LowPriToRow {
  implicit def fromShow[T](implicit show: Show[T]): ToRow[T] =
    ToRow {
      t ⇒ Seq(t.show)
    }
}

object ToRow
  extends LowPriToRow {

  def apply[T](fn: T ⇒ Seq[String]): ToRow[T] =
    new ToRow[T] {
      def apply(t: T): Seq[String] = fn(t)
    }

  implicit def caseClass[T, L <: HList](
    implicit
    g: Generic.Aux[T, L],
    r: Lazy[ToRow[L]]
  ):
    ToRow[T] =
    ToRow {
      t ⇒
        r.value(g.to(t))
    }

  implicit def cons[H, T <: HList](
    implicit
    rh: Lazy[ToRow[H]],
    rt: Lazy[ToRow[T]]
  ):
    ToRow[H :: T] =
    ToRow {
      case h :: t ⇒
        (
          rh.value(h) ++
          rt.value(t)
        )
        .toList
    }

  implicit val hnil: ToRow[HNil] = ToRow { _ ⇒ Nil }
}
