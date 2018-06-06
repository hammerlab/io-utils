package org.hammerlab.lines

import hammerlab.show._

case class Line(str: String, level: Level = 0) {
  def indent: Line = copy(level = level++)
  def prepend(s: String) = Line(s + str, level)
  def apppend(s: String) = Line(str + s, level)
}

object Line {
  implicit def show(implicit i: Indent): Show[Line] =
    Show {
      case Line(str, level) ⇒
        implicit val l = level
        show"$level$str"
    }
}
