package org.hammerlab.io.print

import hammerlab.show._
import org.hammerlab.io.print

/**
 * Interface for groups of [[Line]]s as toString-like representations of objects.
 *
 * Implicitly created from a single [[String]], a group of [[Line]]s, or as the output of application of a [[ToLines]]
 * instance.
 *
 * Funneled through [[Printer]] interfaces to output to files/stdout.
 */
sealed trait Lines extends Any {
  def lines: Iterator[Line]
  def indent: Lines = lines.map(_.indent).toIterable
}

object Lines {

  /**
   * Construct [[Lines]] from a single [[String]]
   */
  case class Single(line: String)
    extends AnyVal
      with Lines {
    override def lines: Iterator[Line] = Iterator(Line(line))
  }
  implicit def fromString(line: String): Single = Single(line)

  /**
   * Construct [[Lines]] from a collection of [[Line]]s
   */
  case class Multiple(v: Iterable[Line])
    extends AnyVal
      with Lines {
    override def lines: Iterator[Line] = v.iterator
  }
  implicit def fromLines(lines: Iterable[Line]): Multiple = Multiple(lines)

  /**
   * Construct a [[Lines]] from other [[Lines]] instances
   */
  def apply(lines: Lines*): Lines = lines.flatMap(_.lines)

  implicit def unwrap(lines: Lines): Iterator[Line] = lines.lines

  /**
   * `.showLines` syntax: convert an object to [[Lines]] and newline-join them to a [[String]]
   */
  implicit class LinesOps[T](val t: T) extends AnyVal {
    def showLines(implicit lines: ToLines[T], indent: Indent): String = show.apply(lines(t))
  }

  /**
   * Construct a [[Lines]] from any object with a [[ToLines]]
   */
  implicit def toLines[T](t: T)(implicit lines: ToLines[T]): Lines = lines(t)

  /**
   * Construct a [[Lines]] from a collection of [[ToLines]]-able objects
   */
  implicit def iterableToLines[T](ts: Iterable[T])(implicit lines: ToLines[T]): Lines = ts.flatMap(lines(_).lines)

  /**
   * Implicitly unify a collection of [[Lines]], or flatten into a collection of [[Line]]s
   */
  implicit def wrapLines(lines: Iterable[Lines]): Lines = lines.flatMap(_.lines)
  implicit def unwrapLines(lines: Lines): Iterable[Line] = lines.lines.toIterable

  def indent(lines: print.Lines*): Lines = lines.map(_.indent)

  /**
   * Default [[Show]] behavior: materialize indentation-levels and join with newlines
   */
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
