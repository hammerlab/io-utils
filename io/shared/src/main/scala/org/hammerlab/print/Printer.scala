package org.hammerlab.print

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

  protected def showLine(line: String): Unit = ps.println(line)

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

  def apply(os: Lines*): Unit =
    os foreach {
      _.foreach {
        showLine
      }
    }

  def close(): Unit = ps.close()
}

object Printer
  extends PrinterCompanion {

  implicit def makePrinter(ps: PrintStream)(implicit indent: Indent): Printer = Printer(ps)

  implicit def unwrap(p: Printer): PrintStream = p.ps
}
