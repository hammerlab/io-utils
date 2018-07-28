package org.hammerlab.lines.limit

import caseapp.core.Error.UnrecognizedValue
import caseapp.core.argparser._
import cats.Show
import cats.syntax.show._

sealed trait Limit
case object Unlimited      extends Limit
case  class Max(size: Int) extends Limit {
  override def toString: String = size.toString
}

object Limit {
  case class InvalidLimitException(n: Int)
    extends IllegalArgumentException(
      s"Limit must be >= 0 (or -1 to signify no limit)"
    )

  implicit def apply(n: Int): Limit =
    if (n == -1)
      Unlimited
    else if (n < 0)
      throw InvalidLimitException(n)
    else
      Max(n)

  def unapply(limit: Limit): Option[Int] =
    limit match {
      case Max(limit) ⇒ Some(limit)
      case _ ⇒ None
    }

  implicit val parser: ArgParser[Limit] =
    SimpleArgParser.from("sample size") {
      str ⇒
        if (str == "-")
          Right(Unlimited)
        else
          try {
            Right(
              apply(
                str.toInt
              )
            )
          } catch {
            case e: NumberFormatException ⇒ Left(UnrecognizedValue(str))
            case InvalidLimitException(_) ⇒ Left(UnrecognizedValue(str))
          }
    }

  implicit def show(implicit s: Show[Int]): Show[Limit] =
    Show.show {
      case Unlimited ⇒ "-"
      case Max(limit) ⇒ limit.show
    }
}
