package org.hammerlab.io.lines

import hammerlab.show._
import org.hammerlab.io.lines

sealed abstract class Indent(override val toString: String)
  extends Serializable

object Indent {
  implicit def show(implicit level: Level): Show[Indent] =
    Show {
      _.toString * level
    }
}

case object tab extends Indent("\t")

trait instances {
  val tab = lines.tab
  val  spaces = new spaces(2) {
    val   two = lines.spaces(2)
    val  four = lines.spaces(4)
    val eight = lines.spaces(8)
  }
  val spaces2 = lines.spaces.two
  val spaces4 = lines.spaces.four
  val spaces8 = lines.spaces.eight
}

trait implicits {
  implicit val tab = lines.tab
  implicit val  spaces = new spaces(2) {
    implicit val   two = lines.spaces(2)
    implicit val  four = lines.spaces(4)
    implicit val eight = lines.spaces(8)
  }
  implicit val spaces2 = lines.spaces.two
  implicit val spaces4 = lines.spaces.four
  implicit val spaces8 = lines.spaces.eight
}

case class spaces(n: Int) extends Indent(" " * n)
object spaces {
  val   two = spaces(2)
  val  four = spaces(4)
  val eight = spaces(8)
}
