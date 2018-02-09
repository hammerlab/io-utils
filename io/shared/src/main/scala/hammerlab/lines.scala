package hammerlab

import hammerlab.print.Printer
import org.hammerlab.io.lines.Lines.LinesOps
import org.hammerlab.io

object lines {
  def indent(lines: Lines*): Lines = Lines.indent(lines: _*)
  def indent(fn: ⇒ Unit)(implicit printer: Printer): Unit = printer.ind { fn }

  implicit def makeLinesOps[T](t: T): LinesOps[T] = new LinesOps(t)

  type Indent = io.lines.Indent

  type Line = io.lines.Line
   val Line = io.lines.Line

  type Lines = io.lines.Lines
   val Lines = io.lines.Lines

  type ToLines[T] = io.lines.ToLines[T]
   def ToLines[T] = io.lines.ToLines[T] _

  type Level = io.lines.Level
   val Level = io.lines.Level

   trait generic extends io.lines.generic
  object generic extends generic
}
