package hammerlab

import org.hammerlab.io
import org.hammerlab.io.print.Lines.LinesOps
import org.hammerlab.io.print.{ CanPrint, Lines }

object print extends CanPrint {
  type Print[T] = io.print.Print[T]

  type Printer = io.print.Printer
   val Printer = io.print.Printer

  type SampleSize = io.print.SampleSize
   val SampleSize = io.print.SampleSize

  type CanPrint = io.print.CanPrint

  def indent(lines: Lines*): Lines = Lines.indent(lines: _*)

  implicit def makeLinesOps[T](t: T): LinesOps[T] = new LinesOps(t)

  type Indent = io.print.Indent

  type Line = io.print.Line
  val Line = io.print.Line

  type Lines = io.print.Lines
  val Lines = io.print.Lines

  type ToLines[T] = io.print.ToLines[T]
  def ToLines[T] = io.print.ToLines[T] _
}
