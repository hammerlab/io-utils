package org.hammerlab.lines

import hammerlab.show._
import org.hammerlab.lines
import Indent.spaces

sealed abstract class Indent(override val toString: String)
  extends Serializable

case object tab extends Indent("\t")

trait implicits {
  implicit val tab = lines.tab

  trait Has2 { implicit val `2` = Indent.spaces(2) }
  trait Has4 { implicit val `4` = Indent.spaces(4) }
  trait Has8 { implicit val `8` = Indent.spaces(8) }

  /**
   * Default indentation level with spaces is 2:
   *
   * {{{
   * import hammerlab.indent.spaces
   * }}}
   *
   * This object also contains implicit instances for 2-, 4-, and 8-spaces:
   *
   * {{{
   * import hammerlab.indent.spaces.`4`
   * }}}
   */
  implicit val spaces:
    Indent
      with Has2
      with Has4
      with Has8 =
    new spaces(2)
      with Has2
      with Has4
      with Has8

  implicit val spaces2 = Indent.spaces.`2`
  implicit val spaces4 = Indent.spaces.`4`
  implicit val spaces8 = Indent.spaces.`8`
}

object Indent {
  case class spaces(n: Int) extends Indent(" " * n)
  object spaces {
    val `2` = spaces(2)
    val `4` = spaces(4)
    val `8` = spaces(8)
  }
}
