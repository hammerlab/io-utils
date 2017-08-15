package org.hammerlab.io

import cats.Show.show

class Delimiter(override val toString: String)

trait SelfImplicit {
  implicit val self: this.type = this
}

object Delimiter {
  def apply(str: String): Delimiter = new Delimiter(str)

  implicit def unwrap(delimiter: Delimiter): String = delimiter.toString

  implicit val showDelimiter: cats.Show[Delimiter] = show(_.toString)

  object comma extends Delimiter(",") with SelfImplicit
  object commaSpace extends Delimiter(", ") with SelfImplicit
  object space extends Delimiter(" ") with SelfImplicit
  object tab extends Delimiter("\t") with SelfImplicit
}
