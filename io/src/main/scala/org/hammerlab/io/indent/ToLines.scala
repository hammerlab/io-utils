package org.hammerlab.io.indent

import hammerlab.show._
import org.hammerlab.io.print.Lines
import shapeless._

import scala.reflect.ClassTag

trait ToLines[T] {
  def apply(t: T): Lines
}

object ToLines {

  def apply[T](fn: T ⇒ Lines): ToLines[T] =
    new ToLines[T] {
      override def apply(t: T): Lines = fn(t)
    }

  implicit def fromShow[T](implicit show: Show[T]): ToLines[T] = apply(t ⇒ Lines(show(t)))

  implicit class LinesOps(val lines: Iterable[Line]) extends AnyVal {
    def indent: Iterable[Line] = lines.map(_.indent)
  }

  import org.hammerlab.io.print

  def indent(lines: print.Lines*): Iterable[Line] = lines.flatMap(_.lines).map(_.indent)

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
