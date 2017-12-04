package hammerlab

import org.hammerlab.io
import org.hammerlab.io.print.CanPrint
import org.hammerlab.io.print.Lines.LinesOps

object print extends CanPrint {
  type Printer = io.print.Printer
   val Printer = io.print.Printer

  type SampleSize = io.print.SampleSize
   val SampleSize = io.print.SampleSize

  type CanPrint = io.print.CanPrint

  def indent(lines: Lines*): Lines = Lines.indent(lines: _*)
  def indent(fn: ⇒ Unit)(implicit printer: Printer): Unit = printer.ind { fn }

  implicit def makeLinesOps[T](t: T): LinesOps[T] = new LinesOps(t)

  type Indent = io.print.Indent

  type Line = io.print.Line
  val Line = io.print.Line

  type Lines = io.print.Lines
  val Lines = io.print.Lines

  type ToLines[T] = io.print.ToLines[T]
  def ToLines[T] = io.print.ToLines[T] _
}
