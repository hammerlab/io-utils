package org.hammerlab.print

import org.hammerlab.lines.Lines

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
}
