package org.hammerlab.io.print

import hammerlab.iterator._
import hammerlab.show._

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
}

object ToLines
  extends LowPriToLines {

  /**
   * Given a [[Show]] for a type, generate a single-line [[Lines]] representation
   */
  implicit def fromShow[T](implicit show: Show[T]): ToLines[T] = apply(t ⇒ Lines(show(t)))
}
