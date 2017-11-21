package org.hammerlab.io

import java.io.PrintStream

import hammerlab.path._

/**
 * [[PrintStream]]-wrapper requiring [[Show]]s and providing utilities for printing collections of items
 */
case class Printer(ps: PrintStream)(
    implicit
    _indent: Indent = hammerlab.indent.tab
) {

  val token = _indent.token

  private def line(s: String): Unit = {
    ps.print(_indent)
    ps.println(s)
  }

  def apply(os: Shown*): Unit =
    os.foreach {
      case Single(s) ⇒ line(s)
      case Showns(ss) ⇒ ss.foreach { line }
    }

  def indent[T](body: ⇒ Unit): Unit = _indent { body }

  def indent(showns: Shown*): Showns =
    Showns(
      showns.flatMap {
        case Single(s) ⇒ Seq(s"$token$s")
        case Showns(values) ⇒ values.map(s ⇒ s"$token$s")
      }
    )

  def printSamples[T: Show](samples: Iterable[T],
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

  def printList[T: Show](list: Iterable[T],
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
  implicit def unmakePrinter(p: Printer): PrintStream = p.ps

  def apply(path: Path)(implicit indent: Indent): Printer = apply(Some(path))

  def apply(path: Option[Path])(implicit indent: Indent): Printer =
    path match {
      case Some(path) ⇒
        new PrintStream(path.outputStream)
      case None ⇒
        System.out
    }
}




