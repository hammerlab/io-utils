package org.hammerlab.io.print

import java.io.PrintStream

import hammerlab.indent.Indent
import hammerlab.show._
import org.hammerlab.io.indent.Line

case class StreamPrinter(ps: PrintStream)(
    implicit val indent: Indent
)
  extends Printer {
  override def showLine(line: Line): Unit = ps.println(line.show)
  override def close(): Unit = ps.close()
}
