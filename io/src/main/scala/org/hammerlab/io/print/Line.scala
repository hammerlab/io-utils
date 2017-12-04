package org.hammerlab.io.print

import hammerlab.show._

case class Line(str: String, level: Level = 0) {
  def indent: Line = copy(level = level++)
}

object Line {
  implicit def wrap(s: String): Line = Line(s)
  implicit def show(implicit indent: Indent): Show[Line] =
    Show {
      case Line(str, level) ⇒
        implicit val l = level
        show"$indent$str"
    }
}
