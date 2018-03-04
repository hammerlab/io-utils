package org.hammerlab.exception

import hammerlab.show._

case class StackTraceElem(declaringClass: String,
                          methodName: String,
                          fileName: String,
                          lineNumber: Int)

object StackTraceElem {
  implicit def fromStackTraceElement(e: StackTraceElement): StackTraceElem =
    StackTraceElem(
      e.getClassName,
      e.getMethodName,
      e.getFileName,
      e.getLineNumber
    )

  implicit val show: Show[StackTraceElem] =
    Show {
      case StackTraceElem(
        declaringClass,
        methodName,
        fileName,
        lineNumber
      ) ⇒
        s"at $declaringClass.$methodName($fileName:$lineNumber)"
    }
}
