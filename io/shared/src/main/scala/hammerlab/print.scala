package hammerlab

import org.hammerlab.print.CanPrint

object print extends CanPrint {
  import org.hammerlab.{ print ⇒ p }

  type Printer = p.Printer
   val Printer = p.Printer

  type CanPrint = p.CanPrint
}
