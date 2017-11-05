package org.hammerlab.io

import cats.Show.{ Shown ⇒ CatsShown }

/**
 * Wrapper for [[cats.Show]] that automatically accepts [[String]]s, [[Int]]s, and [[Long]]s
 */
trait Show[T] {
  def show(f: T): String
}

object Show {
  implicit val string: Show[String] = fromCats(cats.implicits.catsStdShowForString)
  implicit val int: Show[Int] = fromCats(cats.implicits.catsStdShowForInt)
  implicit val long: Show[Long] = fromCats(cats.implicits.catsStdShowForLong)

  implicit def fromCats[T](implicit wrapped: cats.Show[T]): Show[T] = new Show[T] {
    override def show(t: T): String = wrapped show t
  }

  implicit class Ops[T](t: T) {
    def show(implicit s: Show[T]): String = s show t
  }
}

/**
 * Wrapper for [[cats.Show.Shown]] that automatically accepts [[String]]s
 */
final case class Shown(override val toString: String) extends AnyVal

object Shown {
  implicit def mat[A](x: A)(implicit z: cats.Show[A]): Shown = Shown(z show x)
  implicit def fromCats(shown: CatsShown): Shown = Shown(shown.toString)
  implicit def fromString(str: String): Shown = Shown(str)
}
