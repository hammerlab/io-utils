package org.hammerlab.io.print

import scala.collection.mutable.ArrayBuffer

case class LinesPrinter(implicit val _indent: Indent)
  extends Printer {
  val lines = ArrayBuffer[Line]()
  override def showLine(line: Line): Unit = lines += line
}
