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

  /**
   * Default instance for product-types, overridden by more specific instances (e.g. based on [[Show]]s)
   */
  implicit def generic[T, L <: HList](implicit
                                      gen: Generic.Aux[T, L],
                                      tLines: ToLines[L],
                                      ct: ClassTag[T]): ToLines[T] =
    apply(
      t ⇒
        Lines(
          ct.runtimeClass.getSimpleName + "(",
          indent(
            tLines(gen.to(t))
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
}

object ToLines
  extends LowPriToLines {

  /**
   * Given a [[Show]] for a type, generate a single-line [[Lines]] representation
   */
  implicit def fromShow[T](implicit show: Show[T]): ToLines[T] = apply(t ⇒ Lines(show(t)))
}
