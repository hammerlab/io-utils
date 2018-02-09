package org.hammerlab.exception

case class StackTraceElem(declaringClass: String,
                          methodName: String,
                          fileName: String,
                          lineNumber: Int) {
  override def toString: String =
    s"at $declaringClass.$methodName($fileName:$lineNumber)"
}

object StackTraceElem {
  implicit def fromStackTraceElement(e: StackTraceElement): StackTraceElem =
    StackTraceElem(
      e.getClassName,
      e.getMethodName,
      e.getFileName,
      e.getLineNumber
    )
}
