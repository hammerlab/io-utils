package org.hammerlab.io.print

import caseapp.core.ArgParser
import caseapp.core.ArgParser.instance

case class Limit(size: Option[Int]) {
  def <(other: Long): Boolean =
    size.exists(_ < other)
}

object Limit {
  implicit def apply(size: Int): Limit = Limit(Some(size))

  implicit val parser: ArgParser[Limit] =
    instance("sample size") {
      str ⇒
        Right(
          Limit(
            Some(
              str.toInt
            )
          )
        )
    }
}
