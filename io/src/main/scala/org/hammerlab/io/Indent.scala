package org.hammerlab.io

/**
 * Interface for an indent [[token]] (e.g. "\t", "  ") and level (repetitions of [[token]]
 */
/*
sealed abstract class Indent(val token: String)
  extends Serializable {
  var level: Int
  def unary_+ : this.type = { level += 1; this }
  def unary_- : this.type = { level -= 1; this }
  def ++ : this.type = { level += 1; this }
  def -- : this.type = { level -= 1; this }
  override def toString: String = token * level
  def apply[T](fn: ⇒ T): T = {
    ++
    val t = fn
    --
    t
  }
}

case class tabs(var level: Int) extends Indent("\t")

sealed abstract class spaces(val width: Int)
  extends Indent(" " * width)
    with Serializable

case class spaces2(var level: Int) extends spaces(2)
case class spaces4(var level: Int) extends spaces(4)
case class spaces8(var level: Int) extends spaces(8)

object Indent {

  implicit def showIndent[I <: Indent]: cats.Show[I] = cats.Show.show { _.toString }

  import org.hammerlab.io
  trait instances extends Serializable {
    implicit def tab = io.tabs(0)
    def tabs(n: Int = 0) = io.tabs(n)

    object spaces {
      implicit def two = io.spaces2(0)
      implicit def four = io.spaces4(0)
    }
  }
  object instances extends instances

  implicit def unwrap(indent: Indent): String = indent.toString
}
*/
