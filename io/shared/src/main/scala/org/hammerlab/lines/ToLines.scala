package org.hammerlab.lines

import hammerlab.iterator._
import hammerlab.show._
import org.hammerlab.lines.Lines.empty

import scala.collection.generic.CanBuildFrom

trait ContraToLines[-T] {
  def apply(t: T): Lines
}

trait ToLines[T] extends ContraToLines[T]

trait LowPriorityToLines {
  /**
   * Convenient instance-definition syntax
   */
  def apply[T](fn: T ⇒ Lines): ToLines[T] =
    new ToLines[T] {
      override def apply(t: T): Lines = fn(t)
    }

  /**
   * Given a [[Show]] for a type, generate a single-line [[Lines]] representation
   */
  implicit def fromShow[T](implicit show: Show[T]): ToLines[T] = apply { t ⇒ Lines(show(t)) }
}

object ToLines
  extends LowPriorityToLines {
  implicit def iterableToLines[
    T,
    I[_] <: Iterable[_]
  ](
    implicit
    lines: ToLines[T],
    ev: I[T] <:< Iterable[T],
    cbf: CanBuildFrom[Iterable[T], Lines, Iterable[Lines]]
  ):
    ToLines[I[T]] =
    ToLines {
      elems: I[T] ⇒
        ev(elems)
          .map[
            Lines,
            Iterable[Lines]
          ](
            elem ⇒
              lines(elem)
          )(
            cbf
          )
    }

  implicit def arrayToLines[T](implicit lines: ToLines[T]): ToLines[Array[T]] =
    ToLines { _.map(lines(_)) }

  implicit def optionToLines[T](implicit lines: ToLines[T]): ToLines[Option[T]] =
    ToLines {
      _
        .map(lines(_))
        .getOrElse(empty)
    }

  implicit def someToLines[T](implicit lines: ToLines[T]): ToLines[Some[T]] =
    ToLines {
      case Some(t) ⇒
        lines(t)
    }

  implicit val bool: ToLines[Boolean] = ToLines { _.toString }
}
