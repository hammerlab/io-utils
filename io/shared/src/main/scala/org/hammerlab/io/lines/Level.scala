package org.hammerlab.io.lines

case class Level(var v: Int) {
  def      ++ : Level = { v += 1; this }
  def      -- : Level = { v -= 1; this }
}

object Level {
  implicit def wrap(v: Int): Level = Level(v)
  implicit def unwrap(level: Level): Int = level.v
}
