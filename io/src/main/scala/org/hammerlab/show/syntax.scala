package org.hammerlab.show

import cats.Show
import cats.Show.ToShowOps
import cats.syntax.ShowSyntax

/**
 * Mix several bits of cats' [[Show]] syntax in one place.
 *
 * In particular, [[ShowSyntax]] is missing from [[cats.syntax.show]]
 */
trait syntax
  extends ShowSyntax
    with ToShowOps {

  implicit val showInt = cats.instances.int.catsStdShowForInt
  implicit val showLong = cats.instances.long.catsStdShowForLong
  implicit val showString = cats.instances.string.catsStdShowForString

  implicit def ShowApplyOps[T] = Ops.ShowApplyOps[T] _

  def show[A](f: A => String): Show[A] = cats.Show.show(f)
  type Show[A] = cats.Show[A]
}

object Ops {
  /**
   * Apply any [[Show]] instance to a corresponding object.
   *
   * Defined here instead of in [[syntax]] so that it can be an [[AnyVal]]
   */
  implicit class ShowApplyOps[T](val s: Show[T]) extends AnyVal {
    def apply(t: T): String = s.show(t)
  }
}
