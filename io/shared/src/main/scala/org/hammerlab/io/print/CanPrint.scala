package org.hammerlab.io.print

import org.hammerlab.io.lines.{ Lines, ToLines }

/**
 * Convenient printing operations in the presence of an implicit [[Printer]]
 */
trait CanPrint {

  /**
   * Alias to avoid colliding with [[Predef.print]] in the case of one argument
   */
  def echo(os: Lines*)(
      implicit printer: Printer
  ): Unit =
    printer(os: _*)

  def write(os: Lines*)(
      implicit printer: Printer
  ): Unit =
    printer(os: _*)

  /**
   * Safe to use `print` name when there are at least two arguments; no risk of collision with [[Predef.print]]
   */
  def print(l1: Lines,
            l2: Lines,
            rest: Lines*)(
      implicit printer: Printer
  ): Unit = {
    printer(l1)
    printer(l2)
    printer(rest: _*)
  }

  def print[T: ToLines](samples: Seq[T],
                        populationSize: Long,
                        header: String,
                        truncatedHeader: Int ⇒ String)(
      implicit
      printer: Printer,
      sampleSize: Limit
  ): Unit =
    printer.limitedPrint(
      samples,
      populationSize,
      header,
      truncatedHeader
    )

  def print[T: ToLines](list: Seq[T],
                        header: String,
                        truncatedHeader: Int ⇒ String)(
      implicit
      printer: Printer,
      sampleSize: Limit
  ): Unit =
    printer.printList(
      list,
      header,
      truncatedHeader
    )
}
