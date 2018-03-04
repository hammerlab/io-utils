package org.hammerlab.parallel.threads

import java.util.concurrent.atomic.AtomicInteger

import org.hammerlab.parallel.makeParallelizer
import org.hammerlab.test.matchers.lines.Line._
import org.hammerlab.test.version.Util.is2_11
import org.hammerlab.test.{ Suite, linesMatch }

abstract class ExceptionTest(numWorkers: Int)
  extends Suite {

  implicit val config = Config(numWorkers)

  var numOpens: AtomicInteger = _
  var numCloses: AtomicInteger = _

  before {
    numOpens = new AtomicInteger(0)
    numCloses = new AtomicInteger(0)
  }

  /**
   * Parallelize some ints, and throw exceptions on indices 1 and 3
   */
  def make(arr: Array[Int]): Array[String] =
    arr
      .zipWithIndex
      .parallel(
        numOpens.incrementAndGet(),
        {
          case (_, 1) ⇒
            throw Index1Exception
          case (elem, 3) ⇒
            throw Index3Exception(elem)
          case (elem, _) ⇒
            elem.toString
        },
        numCloses.incrementAndGet()
      )

  test("except") {
    val exception =
      intercept[ParallelWorkerExceptions[Int]] {
        make(1 to 10 toArray)
      }

    exception
      .exceptions should be(
        Seq(
          ParallelWorkerException((2, 1), 1, Index1Exception),
          ParallelWorkerException((4, 3), 3, Index3Exception(4))
        )
      )

    numOpens.get() should be(numWorkers)
    numCloses.get() should be(numWorkers)

    // Convenience iterator for stepping through lines of the exception's string-representation
    val lines =
      new BufferedIterator[String] {
        var idx = 0
        val lines =
          exception
            .toString
            .split("\n")

        override def head: String = lines(idx)

        override def hasNext: Boolean =
          idx < lines.length

        override def next(): String = {
          val line = head
          idx += 1
          line
        }
      }

    val firstLines =
      lines
        .take(6)
        .mkString("\n")

    if (is2_11)
      firstLines should linesMatch(
        l"org.hammerlab.parallel.threads.ParallelWorkerExceptions: 2 uncaught exceptions thrown in parallel worker threads:",
        l"	org.hammerlab.parallel.threads.Index1Exception$$: foo",
        l"		at org.hammerlab.parallel.threads.Index1Exception$$.<clinit>(ExceptionTest.scala:-1)",
        l"		at org.hammerlab.parallel.threads.ExceptionTest$$$$anonfun$$make$$4.apply(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.threads.ExceptionTest$$$$anonfun$$make$$4.apply(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.package$$Parallelizer$$$$anonfun$$parallel$$4.apply(package.scala:$d)"
      )
    else
      firstLines should linesMatch(
        l"org.hammerlab.parallel.threads.ParallelWorkerExceptions: 2 uncaught exceptions thrown in parallel worker threads:",
        l"	org.hammerlab.parallel.threads.Index1Exception$$: foo",
        l"		at org.hammerlab.parallel.threads.Index1Exception$$.<clinit>(ExceptionTest.scala:-1)",
        l"		at org.hammerlab.parallel.threads.ExceptionTest.$$anonfun$$make$$4(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.package$$Parallelizer.$$anonfun$$parallel$$4(package.scala:$d)",
        l"		at org.hammerlab.parallel.threads.Parallelizer.org$$hammerlab$$parallel$$threads$$Parallelizer$$$$pollQueue$$1(Parallelizer.scala:$d)"
      )

    while (lines.hasNext && lines.head.startsWith("\t\t"))
      lines.next()

    val nextLines =
      lines
        .take(4)
        .mkString("\n")

    if (is2_11)
      nextLines should linesMatch(
        l"	org.hammerlab.parallel.threads.Index3Exception: bar-4",
        l"		at org.hammerlab.parallel.threads.ExceptionTest$$$$anonfun$$make$$4.apply(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.threads.ExceptionTest$$$$anonfun$$make$$4.apply(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.package$$Parallelizer$$$$anonfun$$parallel$$4.apply(package.scala:$d)"
      )
    else
      nextLines should linesMatch(
        l"	org.hammerlab.parallel.threads.Index3Exception: bar-4",
        l"		at org.hammerlab.parallel.threads.ExceptionTest.$$anonfun$$make$$4(ExceptionTest.scala:$d)",
        l"		at org.hammerlab.parallel.package$$Parallelizer.$$anonfun$$parallel$$4(package.scala:$d)",
        l"		at org.hammerlab.parallel.threads.Parallelizer.org$$hammerlab$$parallel$$threads$$Parallelizer$$$$pollQueue$$1(Parallelizer.scala:$d)"
      )

    while (lines.hasNext && lines.head.startsWith("\t\t"))
      lines.next()

    lines.hasNext should be(false)
  }
}

class OneThreadException
  extends ExceptionTest(1)

class TwoThreadsException
  extends ExceptionTest(2)

case object Index1Exception
  extends Exception("foo")

case class Index3Exception(n: Int)
  extends Exception(s"bar-$n")