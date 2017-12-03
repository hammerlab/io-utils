package org.hammerlab.io

/**
 * Wrapper for [[cats.Show.Shown]] that automatically accepts [[String]]s
 */
case class Shown(override val toString: String) extends AnyVal

object Shown {

  import cats.Show.{ Shown ⇒ CatsShown }

//  implicit def makeShowns[A](xs: Iterable[A])(implicit z: Show[A]): Shown = Showns(xs.map(z show))
  implicit def single[A](x: A)(implicit z: Show[A]): Shown = Shown(z show x)
  implicit def fromCats(shown: CatsShown): Shown = Shown(shown.toString)
  implicit def fromString(str: String): Shown = Shown(str)
}
