package org.hammerlab.io

import java.io.{ Closeable, PrintStream }

import hammerlab.path._
import hammerlab.show._
import org.hammerlab.io
import org.hammerlab.io.indent.{ Indent, Line, ToLines }
import org.hammerlab.io.print.Lines

import scala.collection.mutable.ArrayBuffer

/**
 * [[PrintStream]]-wrapper requiring [[Show]]s and providing utilities for printing collections of items
 */
abstract class Printer
  extends Closeable {

  def showLine(line: Line): Unit

  def indent(lines: Lines*): Lines = lines.flatMap(_.lines).map(_.indent)

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
          io.indent.ToLines.indent(
            Lines.iterableToLines(samples.take(size)),
            "…"
          )
        )
      case _ ⇒
        apply(
          header,
          io.indent.ToLines.indent(
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

case class StreamPrinter(ps: PrintStream)(
    implicit val indent: Indent
)
  extends Printer {
  override def showLine(line: Line): Unit = ps.println(line.show)
  override def close(): Unit = ps.close()
}

case class LinesPrinter(implicit val _indent: Indent)
  extends Printer {
  val lines = ArrayBuffer[Line]()
  override def showLine(line: Line): Unit = lines += line
}

class LinePrint[T](val t: T)(
    implicit override val _indent: Indent
)
  extends LinesPrinter {

}

object LinePrint {
  implicit def apply[T](fn: T ⇒ LinePrint[T]): ToLines[T] =
    ToLines {
      fn(_).lines
    }
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




