package org.hammerlab.io.print

import java.io.{ ByteArrayOutputStream, PrintStream }

class PrinterJSTest extends PrinterTest {
  case class BytesPrinter() extends TestPrinter {
    val bytes = new ByteArrayOutputStream()
    val ps = new PrintStream(bytes)
    val printer = Printer(ps)
    def read: String = {
      printer.close()
      new String(bytes.toByteArray)
    }
  }
  def printer = BytesPrinter()
}
