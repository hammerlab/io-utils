package org.hammerlab.io.print

import java.io.{ Closeable, PrintStream }

import hammerlab.monoid._
import hammerlab.lines._
import hammerlab.show._

/**
 * Interface for writing [[hammerlab.lines.Line]]s with configurable indentation levels
 */
case class Printer(ps: PrintStream)(
    implicit val _indent: Indent
)
  extends Closeable {

  protected def showLine(line: Line): Unit = ps.println(line.show)

  implicit val level = Level(0)

  /**
   * Aliases for underlying [[apply]]
   *
   * [[print]] requires at least two arguments to avoid confusion with [[Predef.print]]
   */
  def  echo(os: Lines*): Unit = apply(os: _*)
  def write(os: Lines*): Unit = apply(os: _*)
  def print(l1: Lines,
            l2: Lines,
            rest: Lines*): Unit = apply(l1, l2, rest)

  def ind(fn: ⇒ Unit): Unit = {
    level++;
    fn
    level--
  }

  def apply(os: Lines*): Unit =
    os foreach {
      _
        .lines
        .foreach {
          line ⇒
            showLine(
              line.copy(
                level = line.level |+| level
              )
            )
        }
    }

  def limitedPrint[T: ToLines](elems: Iterable[T],
                               populationSize: Long,
                               header: String,
                               truncatedHeader: Int ⇒ String)(
      implicit
      limit: Limit
  ): Unit = {
    limit match {
      case Limit(Some(0)) ⇒
        // No-op
      case Limit(Some(size))
        if size + 1 < populationSize ⇒
        apply(
          truncatedHeader(size),
          indent(
            elems.take(size),
            "…"
          )
        )
      case _ ⇒
        apply(
          header,
          indent(
            elems
          )
        )
    }
  }

  def printList[T: ToLines](elems: Iterable[T],
                            header: String,
                            truncatedHeader: Int ⇒ String)(
      implicit
      limit: Limit
  ): Unit =
    limitedPrint(
      elems,
      elems.size,
      header,
      truncatedHeader
    )

  def close(): Unit = ps.close()
}

object Printer
  extends PrinterCompanion {

  implicit def makePrinter(ps: PrintStream)(implicit indent: Indent): Printer = Printer(ps)

  implicit def unwrap(p: Printer): PrintStream = p.ps
}
