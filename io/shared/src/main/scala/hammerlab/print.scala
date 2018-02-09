package hammerlab

import org.hammerlab.io
import org.hammerlab.io.print.CanPrint

object print extends CanPrint {
  type Printer = io.print.Printer
   val Printer = io.print.Printer

  type Limit = io.print.Limit
   val Limit = io.print.Limit

  type CanPrint = io.print.CanPrint
}
