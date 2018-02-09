package org.hammerlab.io

case class Delimiter(override val toString: String)

trait SelfImplicit {
  implicit val self: this.type = this
}

object Delimiter {
  implicit def unwrap(delimiter: Delimiter): String = delimiter.toString

  import cats.Show.show

  implicit val showDelimiter: cats.Show[Delimiter] = show(_.toString)

  object comma extends Delimiter(",") with SelfImplicit
  object commaSpace extends Delimiter(", ") with SelfImplicit
  object space extends Delimiter(" ") with SelfImplicit
  object tab extends Delimiter("\t") with SelfImplicit
}
