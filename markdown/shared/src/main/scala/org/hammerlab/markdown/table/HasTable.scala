package org.hammerlab.markdown.table

import hammerlab.lines._
import org.hammerlab.markdown.table.Header.Entry

trait HasTable {

  object sep {
    val   left = "---"
    val center = ":-:"
    val  right = "--:"
  }

  case class InvalidFieldOverrides(overrides: Iterable[String])
    extends IllegalArgumentException(
      s"Invalid field overrides: ${overrides.mkString(",")}"
    )

  def apply[T](
    ts: Seq[T],
    overrides: (Symbol, String)*
  )(
    implicit
     toRow:  ToRow[T],
    header: Header[T]
  ):
    Lines = {

    val overrideMap =
      overrides
        .map {
          case (n, display) ⇒
            n.name → display
        }
        .toMap

    val headers = header()
    val headerMap =
      headers
        .map {
          case Entry(name, display) ⇒
            name → display
        }
        .toMap

    overrideMap
      .keys
      .filter(!headerMap.contains(_)) match {
        case missings
          if missings.nonEmpty ⇒
          throw InvalidFieldOverrides(missings)
        case _ ⇒
      }

    val hdr =
      headers
        .map {
          case Entry(name, display) ⇒
            overrideMap.getOrElse(name, display)
        }
    val seps = Seq.fill(hdr.size)(sep.left)
    val rows = ts.map(toRow(_)).toList
    val lines =
       hdr ::
      seps ::
      rows
    Lines(
      lines
        .map(
          _.mkString(
             "| ",
            " | ",
            " |"
          ):
          Lines
        )
    )
  }

  implicit class MarkdownTableOps[T](ts: Seq[T]) {
    def mdTable(
      overrides: (Symbol, String)*
    )(
      implicit
      toRow:  ToRow[T],
      header: Header[T]
    ):
    Lines =
      apply(
        ts,
        overrides: _*
      )
  }
}
