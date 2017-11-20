package hammerlab

object print {
  type Print[T] = org.hammerlab.io.Print[T]
  val  Print = org.hammerlab.io.Print

  type Printer = org.hammerlab.io.Printer
  val  Printer = org.hammerlab.io.Printer

  type SampleSize = org.hammerlab.io.SampleSize
  val  SampleSize = org.hammerlab.io.SampleSize

  type CanPrint = org.hammerlab.io.CanPrint
}
