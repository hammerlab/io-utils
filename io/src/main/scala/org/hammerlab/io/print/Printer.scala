package org.hammerlab.io.print

import java.io.{ Closeable, PrintStream }

import hammerlab.monoid._
import hammerlab.path._
import hammerlab.print._
import hammerlab.show._

/**
 * Interface for writing [[Line]]s with configurable indentation levels
 */
case class Printer(ps: PrintStream)(
    implicit val _indent: Indent
)
  extends Closeable {

  protected def showLine(line: Line): Unit = ps.println(line.show)

  implicit val level = Level(0)

  /**
   * Aliases for underlying [[apply]]
   *
   * [[print]] requires at least two arguments to avoid confusion with [[Predef.print]]
   */
  def  echo(os: Lines*): Unit = apply(os: _*)
  def write(os: Lines*): Unit = apply(os: _*)
  def print(l1: Lines,
            l2: Lines,
            rest: Lines*): Unit = apply(l1, l2, rest)

  def ind(fn: ⇒ Unit): Unit = {
    level++;
    fn
    level--
  }

  def apply(os: Lines*): Unit =
    os foreach {
      _
        .lines
        .foreach {
          line ⇒
            showLine(
              line.copy(
                level = line.level |+| level
              )
            )
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
            samples.take(size),
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

  def close(): Unit = ps.close()
}

object Printer {

  implicit def makePrinter(ps: PrintStream)(implicit indent: Indent): Printer = Printer(ps)

  def apply(path: Path)(implicit indent: Indent): Printer = apply(Some(path))

  def apply(path: Option[Path])(implicit indent: Indent): Printer =
    path match {
      case Some(path) ⇒
        new PrintStream(path.outputStream)
      case None ⇒
        System.out
    }

  implicit def unwrap(p: Printer): PrintStream = p.ps
}
