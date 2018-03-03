package org.hammerlab.lines.limit

import caseapp.core.argparser._
import cats.Show
import cats.syntax.show._

sealed trait Limit
case object UnLimited      extends Limit
case  class Max(size: Int) extends Limit {
  override def toString: String = size.toString
}

object Limit {
  implicit def apply(size: Int): Limit = Max(size)

  def unapply(limit: Limit): Option[Int] =
    limit match {
      case Max(limit) ⇒ scala.Some(limit)
      case _ ⇒ scala.None
    }

  implicit val parser: ArgParser[Limit] =
    SimpleArgParser.from("sample size") {
      str ⇒
        Right(
          Max(
            str.toInt
          )
        )
    }

  implicit def show(implicit s: Show[Int]): Show[Limit] =
    Show.show {
      case UnLimited ⇒ "-"
      case Max(limit) ⇒ limit.show
    }
}
