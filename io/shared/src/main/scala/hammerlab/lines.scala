package hammerlab

import org.hammerlab.io
import org.hammerlab.io.lines.HasLines

object lines
  extends HasLines {

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
