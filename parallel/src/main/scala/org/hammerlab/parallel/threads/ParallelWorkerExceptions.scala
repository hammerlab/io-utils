package org.hammerlab.parallel.threads

import hammerlab.indent.implicits.tab
import hammerlab.lines._
import hammerlab.show._
import org.hammerlab.exception.Error
import org.hammerlab.parallel.threads.ParallelWorkerExceptions._

case class ParallelWorkerExceptions[T](exceptions: Exceptions[T])
  extends RuntimeException(
    exceptions.showLines
  )

object ParallelWorkerExceptions {
  type Exceptions[T] = Seq[ParallelWorkerException[T]]
  implicit def lines[T]: ToLines[Exceptions[T]] =
    ToLines {
      es ⇒
      Lines(
        s"${es.length} uncaught exceptions thrown in parallel worker threads:",
        indent {
          for {
            ParallelWorkerException(_, _, exception) ← es
          } yield
            Error(exception)
        }
      )
    }

}
