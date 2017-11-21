package org.hammerlab.io

/**
 * Wrapper for [[cats.Show.Shown]] that automatically accepts [[String]]s
 */
sealed trait Shown extends Any
final case class Single(override val toString: String) extends AnyVal with Shown
final case class Showns(values: Iterable[String]) extends AnyVal with Shown

object Shown {

  import cats.Show.{ Shown ⇒ CatsShown }

  implicit def makeShowns[A](xs: Iterable[A])(implicit z: Show[A]): Shown = Showns(xs.map(z show))
  implicit def single[A](x: A)(implicit z: Show[A]): Shown = Single(z show x)
  implicit def fromCats(shown: CatsShown): Shown = Single(shown.toString)
  implicit def fromString(str: String): Shown = Single(str)
}
