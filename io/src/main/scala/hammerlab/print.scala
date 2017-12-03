package hammerlab

import org.hammerlab.io
import org.hammerlab.io.print.{ CanPrint, Printer, SampleSize }

object print extends CanPrint {
  type Print[T] = io.print.Print[T]
//   val Print    = io.print.Print

  type Printer = io.print.Printer
   val Printer = io.print.Printer

  type SampleSize = io.print.SampleSize
   val SampleSize = io.print.SampleSize

  type CanPrint = io.print.CanPrint
}
