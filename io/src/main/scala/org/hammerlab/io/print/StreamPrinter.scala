package org.hammerlab.io.print

import java.io.PrintStream

import hammerlab.show._

/**
 * [[Printer]] that
 * @param ps
 * @param indent
 */
case class StreamPrinter(ps: PrintStream)(
    implicit val indent: Indent
)
  extends Printer {
  override def showLine(line: Line): Unit = ps.println(line.show)
  override def close(): Unit = ps.close()
}

object StreamPrinter {
  implicit def unwrap(p: StreamPrinter): PrintStream = p.ps
}
