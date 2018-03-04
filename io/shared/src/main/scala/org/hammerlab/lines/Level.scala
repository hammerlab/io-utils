package org.hammerlab.lines

import hammerlab.show._

case class Level(v: Int) {
  def ++ : Level = Level(v + 1)
}

object Level {
  implicit def wrap(v: Int): Level = Level(v)
  implicit def unwrap(level: Level): Int = level.v
  implicit def show(implicit indent: Indent): Show[Level] =
    Show { indent.toString * _ }
}
