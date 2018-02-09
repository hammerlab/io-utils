package org.hammerlab.io.print

import java.io.PrintStream

import hammerlab.path.Path
import org.hammerlab.io.lines

trait PrinterCompanion {
  def apply(path: Path)(implicit indent: lines.Indent): Printer = apply(Some(path))

  def apply(path: Option[Path])(implicit indent: lines.Indent): Printer =
    path match {
      case Some(path) ⇒
        new PrintStream(path.outputStream)
      case None ⇒
        System.out
    }
}
