package org.hammerlab.io.print

import scala.collection.mutable.ArrayBuffer

/**
 * [[Printer]] that just accumulates [[Line]]s in an [[ArrayBuffer]]
 */
case class LinesPrinter(implicit val _indent: Indent)
  extends Printer {
  val lines = ArrayBuffer[Line]()
  override def showLine(line: Line): Unit = lines += line
}
