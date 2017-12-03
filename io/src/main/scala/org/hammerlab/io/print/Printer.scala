package org.hammerlab.io.print

import java.io.{ Closeable, PrintStream }

import hammerlab.indent._
import hammerlab.path._
import hammerlab.show._
import org.hammerlab.io.print.Lines._

/**
 * [[PrintStream]]-wrapper requiring [[Show]]s and providing utilities for printing collections of items
 */
abstract class Printer
  extends Closeable {

  def showLine(line: Line): Unit

  def write(os: Lines*): Unit = apply(os: _*)

  def apply(os: Lines*): Unit =
    os.foreach {
      _
        .lines
        .foreach {
          line ⇒
            showLine(line)
        }
    }

  def printSamples[T: ToLines](samples: Iterable[T],
                               populationSize: Long,
                               header: String,
                               truncatedHeader: Int ⇒ String)(
      implicit
      sampleSize: SampleSize
  ): Unit = {
    sampleSize match {
      case SampleSize(Some(0)) ⇒
        // No-op
      case SampleSize(Some(size))
        if size + 1 < populationSize ⇒
        apply(
          truncatedHeader(size),
          indent(
            Lines.iterableToLines(samples.take(size)),
            "…"
          )
        )
      case _ ⇒
        apply(
          header,
          indent(
            samples
          )
        )
    }
  }

  def printList[T: ToLines](list: Iterable[T],
                            header: String,
                            truncatedHeader: Int ⇒ String)(
      implicit
      sampleSize: SampleSize
  ): Unit =
    printSamples(
      list,
      list.size,
      header,
      truncatedHeader
    )

  def close(): Unit = {}
}

object Printer {

  implicit def makePrinter(ps: PrintStream)(implicit indent: Indent): Printer = StreamPrinter(ps)

  def apply(path: Path)(implicit indent: Indent): Printer = apply(Some(path))

  def apply(path: Option[Path])(implicit indent: Indent): Printer =
    path match {
      case Some(path) ⇒
        new PrintStream(path.outputStream)
      case None ⇒
        System.out
    }
}




