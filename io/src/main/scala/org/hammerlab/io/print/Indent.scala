package org.hammerlab.io.print

import hammerlab.show._
import org.hammerlab.io.print

sealed abstract class Indent(override val toString: String)
object Indent {
  implicit def show(implicit level: Level): Show[Indent] =
    Show {
      _.toString * level
    }
}

case object tab extends Indent("\t")

trait instances {
  val tab = print.tab
  val  spaces = new spaces(2) {
    val   two = print.spaces(2)
    val  four = print.spaces(4)
    val eight = print.spaces(8)
  }
  val spaces2 = print.spaces.two
  val spaces4 = print.spaces.four
  val spaces8 = print.spaces.eight
}

trait implicits {
  implicit val tab = print.tab
  implicit val  spaces = new spaces(2) {
    implicit val   two = print.spaces(2)
    implicit val  four = print.spaces(4)
    implicit val eight = print.spaces(8)
  }
  implicit val spaces2 = print.spaces.two
  implicit val spaces4 = print.spaces.four
  implicit val spaces8 = print.spaces.eight
}

case class spaces(n: Int) extends Indent(" " * n)
object spaces {
  val   two = spaces(2)
  val  four = spaces(4)
  val eight = spaces(8)
}
