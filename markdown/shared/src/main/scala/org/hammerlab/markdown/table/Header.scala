package org.hammerlab.markdown.table

import hammerlab.iterator._
import hammerlab.lines._
import hammerlab.show._
import org.hammerlab.markdown.table.Header.Entry

trait Header[T] {
  def apply(): Seq[Entry]
}
object Header {
  case class Entry(name: String, display: String)
  object Entry {
    implicit val show: Show[Entry] = Show { _.display }
  }

  def apply[T](fn: () ⇒ Seq[Entry]): Header[T] =
    new Header[T] {
      def apply(): Seq[Entry] = fn()
    }

  val regex = "[A-Z]?[a-z]+".r
  def display(name: String): String = {
    regex
      .findAllMatchIn(name)
      .sliding2Opt
      .flatMap {
        case (cur, nextOpt) ⇒
          val next = nextOpt.map(_.start).getOrElse(name.length)
          val start = cur.start
          val `match` = cur.matched
          val first = `match`(0).toUpper + `match`.substring(1)
          val end = start + first.length
          if (end != next)
            Seq(
              first,
              name.substring(end, next)
            )
          else
            Seq(
              first
            )

      }
      .mkString(" ")
  }

  import shapeless._
  import shapeless.ops.hlist.ToTraversable
  import shapeless.ops.record._

  implicit def default[T, L <: HList, Out <: HList](
    implicit
    g: LabelledGeneric.Aux[T, L],
    keys: Keys.Aux[L, Out],
    t: ToTraversable.Aux[Out, List, Symbol]
  ):
    Header[T] =
    Header {
      () ⇒
        keys
          .apply
          .toList
          .map(_.name)
          .map(n ⇒ Entry(n, display(n)))
    }
}


