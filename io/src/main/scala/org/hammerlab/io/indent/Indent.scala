package org.hammerlab.io.indent

import hammerlab.show._

sealed abstract class Indent(override val toString: String)
object Indent {
  implicit def show(implicit level: Level): Show[Indent] =
    Show {
      _.toString * level
    }
}

case object tab extends Indent("\t")

trait instances {
  import org.hammerlab.io.indent
  val tab = indent.tab
  val  spaces = new indent.spaces(2) {
    val   two = indent.spaces(2)
    val  four = indent.spaces(4)
    val eight = indent.spaces(8)
  }
  val spaces2 = indent.spaces.two
  val spaces4 = indent.spaces.four
  val spaces8 = indent.spaces.eight
}

trait implicits {
  import org.hammerlab.io.indent
  implicit val tab = indent.tab
  implicit val  spaces = new indent.spaces(2) {
    implicit val   two = indent.spaces(2)
    implicit val  four = indent.spaces(4)
    implicit val eight = indent.spaces(8)
  }
  implicit val spaces2 = indent.spaces.two
  implicit val spaces4 = indent.spaces.four
  implicit val spaces8 = indent.spaces.eight
}

case class spaces(n: Int) extends Indent(" " * n)
object spaces {
  val   two = spaces(2)
  val  four = spaces(4)
  val eight = spaces(8)
}
