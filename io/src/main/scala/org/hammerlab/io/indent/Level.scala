package org.hammerlab.io.indent

case class Level(var v: Int) {
  def unary_+ : Level = { v += 1; this }
  def unary_- : Level = { v -= 1; this }
  def      ++ : Level = { v += 1; this }
  def      -- : Level = { v -= 1; this }
  def apply[T](fn: ⇒ T): T = {
    ++
    val t = fn
    --
    t
  }
}

object Level {
  implicit def wrap(v: Int): Level = Level(v)
  implicit def unwrap(level: Level): Int = level.v
}
