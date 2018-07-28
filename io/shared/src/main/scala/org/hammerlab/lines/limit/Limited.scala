package org.hammerlab.lines.limit

import hammerlab.lines._
import hammerlab.show._

case class Header(msg: String, overflowMsg: String)

case class Limited[T](elems: Iterable[T],
                      size: Long,
                      header: Option[Header])

object Limited {

  def apply[T](elems: Iterable[T]): Limited[T] =
    Limited(
      elems,
      elems.size,
      scala.None
    )

  def apply[T](elems: Iterable[T],
               size: Long): Limited[T] =
    Limited(
      elems,
      size,
      scala.None
    )

  def apply[T](elems: Iterable[T],
               msg: String): Limited[T] =
    apply(
      elems,
      msg,
      msg
    )

  def apply[T](elems: Iterable[T],
               msg: String,
               overflowMsg: String): Limited[T] =
    Limited(
      elems,
      elems.size,
      scala.Some(
        Header(
          msg,
          overflowMsg
        )
      )
    )

  def apply[T](elems: Iterable[T],
               size: Long,
               msg: String): Limited[T] =
    apply(elems, size, msg, msg)

  def apply[T](elems: Iterable[T],
               size: Long,
               msg: String,
               overflowMsg: String): Limited[T] =
    Limited(
      elems,
      size,
      scala.Some(
        Header(
          msg,
          overflowMsg
        )
      )
    )

  implicit def lines[T: Show](implicit limit: Limit): ToLines[Limited[T]] =
    ToLines {
      case Limited(elems, size, header) ⇒
        limit match {
          case Limit(0) ⇒
            Lines.empty
          case Limit(limit)
            if limit + 1 < size ⇒
            header
              .map {
                h ⇒
                  Lines(
                    h.overflowMsg,
                    indent(
                      elems.take(limit),
                      "…"
                    )
                  )
              }
              .getOrElse(
                Lines(
                  elems.take(limit),
                  "…"
                )
              )
          case _ ⇒
            header
              .map {
                h ⇒
                  Lines(
                    h.msg,
                    indent(elems)
                  )
              }
              .getOrElse(
                elems
              )
        }
    }
}
