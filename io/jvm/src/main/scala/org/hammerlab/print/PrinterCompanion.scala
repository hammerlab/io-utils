package org.hammerlab.print

import java.io.PrintStream

import hammerlab.path.Path
import org.hammerlab.lines

trait PrinterCompanion {
  def apply(path: Path)(implicit indent: lines.Indent): Printer = apply(scala.Some(path))

  def apply(path: Option[Path])(implicit indent: lines.Indent): Printer =
    path match {
      case scala.Some(path) ⇒
        new PrintStream(path.outputStream)
      case scala.None ⇒
        System.out
    }
}
