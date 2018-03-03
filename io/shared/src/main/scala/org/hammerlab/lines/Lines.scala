package org.hammerlab.lines

import java.io.PrintStream

import hammerlab.iterator._
import hammerlab.show._
import org.hammerlab.lines.{ Indent ⇒ Ind }

/**
 * Interface for rendering objects to sequences of lines; a more structured version of [[cats.Show]], with support for
 * indentation and multi-line pretty-printing.
 *
 * [[Lines]] can be implicitly created from a single [[String]], a group of [[Lines]]s, or by application of a
 * [[ToLines]] instance.
 */
sealed trait Lines extends Any {
  /*
   * these methods are provided for all types via [[Lines.Ops]], but are denormalized here because that "ops" pattern
   * doesn't work on [[Any]]s
   */
  def showLines(implicit indent: Ind): String = Lines.show.apply(this)
  def show(implicit indent: Ind): String = Lines.show.apply(this)
}

object Lines {

  val empty = Lines()

  def unapply(lines: Lines): Option[Stream[Line]] =
    Some(unrollIndents(lines).toStream)

  /* Node indicating that its descendents should be indented by one level */
  case class Indent(lines: Lines) extends Lines
  object Indent {
    def apply(level: Level, lines: Lines): Lines =
      if (level.v == 0)
        lines
      else
        Indent(
          level - 1,
          Indent(lines)
        )
  }

  implicit def fromLine(line: Line): Lines = Indent(line.level, line.str)

  /**
   * Construct [[Lines]] from a single [[String]]
   */
  private case class Single(line: String)
     extends AnyVal
        with Lines
  implicit def apply(line: String): Lines = Single(line)

  /**
   * Construct a [[Lines]] from other [[Lines]] instances
   */
  private case class Multiple(v: Iterable[Lines])
     extends AnyVal
        with Lines
  def apply(lines: Lines*): Lines = Multiple(lines)
  implicit def wrapLines(lines: Iterable[Lines]): Lines = Multiple(lines)
  implicit def wrapArray(lines: Array[Lines]): Lines = Multiple(lines)

  /**
   * Traverse a tree of [[Lines]] and emit [[Line]]s with [[Line.level indentation-level]] and [[Line.str msg]]
   * components
   */
  def unrollIndents(lines: Lines): Iterator[Line] =
    lines match {
      case Indent(lines) ⇒
        unrollIndents(lines)
          .map {
            case Line(str, level) ⇒
                 Line(str, level++)
          }
      case Single(str) ⇒ Iterator(Line(str))
      case Multiple(lines) ⇒
        lines
          .iterator
          .flatMap(unrollIndents)
    }

  /* Given an [[org.hammerlab.lines.Indent]], serialize a [[Lines]] to [[String]]s */
  implicit def unwrap(lines: Lines)(implicit i: Ind): Iterator[String] =
    unrollIndents(lines).map { toShow(_).show }

  def printlns(lines: Lines*)(implicit i: Ind, ps: PrintStream = System.out): Unit =
    ps.println(Lines(lines).showLines)

  implicit class Ops[T](val t: T) extends AnyVal {
    /* convert an object to [[Lines]] and newline-join them to a [[String]] */
    def showLines(implicit
                  lines: ToLines[T],
                  i: Ind): String = show.apply(lines(t))

    /* Convert an object to [[Lines]] */
    def lines(implicit l: ToLines[T]): Lines = l(t)
  }

  implicit class JoinOps[S](val elems: S) extends AnyVal {
    /**
     * Given an [[Iterable]] of [[Lines]]-able elements, generate [[Lines]] for each element and join them with a
     * delimiter
     */
    def join[T](delimiter: String)(
        implicit
        ev: S <:< Iterable[T],
        lines: ToLines[T]
    ): Lines =
      elems
        .iterator
        .map(lines(_))
        .sliding2Opt
        .map {
          case (l, Some(_)) ⇒ l.append(delimiter)
          case (l,      _ ) ⇒ l
        }
        .toSeq
  }

  implicit class AppendOps(val l: Lines) extends AnyVal {
    def append(s: String): Lines =
      l match {
        case Indent(lines) ⇒ Indent(lines.append(s))
        case Single(line) ⇒ Single(line + s)
        case Multiple(lines) ⇒
          if (lines.isEmpty)
            empty
          else {
            lines
              .dropRight(1)
              .toVector :+
            lines
              .last
              .append(s)
          }
      }
  }

  /**
   * Construct a [[Lines]] from any object with a [[ToLines]]
   */
  implicit def toLines[T](t: T)(implicit lines: ToLines[T]): Lines = lines(t)

  implicit def flattenOpt(lines: Option[Lines]): Lines = lines.getOrElse(empty)

  /**
   * Default [[Show]] behavior: materialize indentation-levels and join with newlines
   */
  implicit def show(implicit indent: Ind): Show[Lines] =
    Show {
      unwrap(_)
        .mkString("\n")
    }
}

trait HasLines {
  def indent(lines: Lines*): Lines = Lines.Indent(lines)

  implicit val LineAppendOps = Lines.AppendOps _
  implicit def LineJoinOps[T] = Lines.JoinOps[T] _
  implicit def LineOps[T] = Lines.Ops[T] _
}
