package org.hammerlab.io.print

import hammerlab.print._
import hammerlab.show._
import shapeless._

import scala.reflect.ClassTag

trait ToLines[T] {
  def apply(t: T): Lines
}

trait LowPriToLines {

  /**
   * Convenient instance-definition syntax
   */
  def apply[T](fn: T ⇒ Lines): ToLines[T] =
    new ToLines[T] {
      override def apply(t: T): Lines = fn(t)
    }

  implicit def fromFn[T](fn: T ⇒ Lines): ToLines[T] = ToLines(fn)

  /**
   * Default instance for product-types, overridden by more specific instances (e.g. based on [[Show]]s)
   */
  implicit def generic[T, L <: HList](implicit
                                      gen: Generic.Aux[T, L],
                                      tLines: Lazy[ToLines[L]],
                                      ct: ClassTag[T]): ToLines[T] =
    apply(
      t ⇒
        Lines(
          ct.runtimeClass.getSimpleName + "(",
          indent(
            tLines.value(gen.to(t))
          ),
          ")"
        )
    )

  implicit def cons[H, T <: HList](implicit
                                   hLines: Lazy[ToLines[H]],
                                   tLines: Lazy[ToLines[T]]): ToLines[H :: T] =
    apply(
      l ⇒
        Lines(
          hLines.value(l.head),
          tLines.value(l.tail)
        )
    )

  implicit val hnil: ToLines[HNil] = apply(_ ⇒ Lines())

  implicit def showSeq[T](implicit
                          showT: ToLines[T],
                          indent_ : Indent): ToLines[Seq[T]] =
    ToLines {
      t ⇒
        if (t.isEmpty)
          "Seq()"
        else
          Lines(
            "Seq(",
            indent(t.map(showT(_)): _*),
            ")"
          )
    }

  implicit def showVec[T](implicit
                          showT: ToLines[T],
                          indent_ : Indent): ToLines[Vector[T]] =
    ToLines {
      t ⇒
        if (t.isEmpty)
          "Seq()"
        else
          Lines(
            "Seq(",
            indent(t.map(showT(_)): _*),
            ")"
          )
    }
}

object ToLines
  extends LowPriToLines {

  /**
   * Given a [[Show]] for a type, generate a single-line [[Lines]] representation
   */
  implicit def fromShow[T](implicit show: Show[T]): ToLines[T] = apply(t ⇒ Lines(show(t)))
}
