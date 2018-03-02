package org.hammerlab.io.lines

import hammerlab.iterator._
import hammerlab.show._
import org.hammerlab.io.lines.Lines.empty

trait ToLines[-T] {
  def apply(t: T): Lines
}

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
  implicit def iterableToLines[T](implicit lines: ToLines[T]): ToLines[Iterable[T]] =
    ToLines { _.map(lines(_)) }

  implicit def arrayToLines[T](implicit lines: ToLines[T]): ToLines[Array[T]] =
    ToLines { _.map(lines(_)) }

  /**
   * Construct a [[Lines]] from a collection of [[ToLines]]-able objects
   */
  implicit def optionToLines[T](implicit lines: ToLines[T]): ToLines[Option[T]] =
    ToLines {
      _
        .map(lines(_))
        .getOrElse(empty)
    }

  implicit val bool: ToLines[Boolean] = ToLines { _.toString }
}
