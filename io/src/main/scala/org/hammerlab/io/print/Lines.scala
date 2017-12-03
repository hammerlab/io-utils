package org.hammerlab.io.print

import hammerlab.indent._
import hammerlab.show._

sealed trait Lines extends Any {
  def lines: Iterator[Line]
  def indent: Lines = lines.map(_.indent).toIterable
}

object Lines {
  case class Single(line: String)
    extends AnyVal
      with Lines {
    override def lines: Iterator[Line] = Iterator(Line(line))
  }
  implicit def fromString(line: String): Single = Single(line)

  case class Multiple(v: Iterable[Line])
    extends AnyVal
      with Lines {
    override def lines: Iterator[Line] = v.iterator
  }
  implicit def fromLines(lines: Iterable[Line]): Multiple = Multiple(lines)

  def apply(lines: Lines*): Lines = lines.flatMap(_.lines)
  implicit def unwrap(lines: Lines): Iterator[Line] = lines.lines

  implicit class LinesOps[T](val t: T) extends AnyVal {
    def showLines(implicit lines: ToLines[T], indent: Indent): String = show.apply(lines(t))
  }

  implicit def toLines[T](t: T)(implicit lines: ToLines[T]): Lines = lines(t)
  implicit def iterableToLines[T](ts: Iterable[T])(implicit lines: ToLines[T]): Lines = ts.flatMap(lines(_).lines)

  implicit def wrapLines(lines: Iterable[Lines]): Lines = lines.flatMap(_.lines)
  implicit def unwrapLines(lines: Lines): Iterable[Line] = lines.lines.toIterable

  implicit def show(implicit indent: Indent): Show[Lines] =
    Show {
      _
        .lines
        .map {
          case Line(line, level) ⇒
            implicit val l = level
            show"$indent$line"
        }
        .mkString("\n")
    }
}
