package org.hammerlab.io.print

import org.hammerlab.test.Suite

class PrinterJVMTest
  extends Suite
    with PrinterTest {

  case class PathPrinter()
    extends TestPrinter {
    val path = tmpPath()
    val printer = Printer(path)
    def read: String = {
      printer.close()
      path.read
    }
  }

  override def printer: TestPrinter = PathPrinter()
}
