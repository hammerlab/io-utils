package org.hammerlab.exception

import org.hammerlab.test.matchers.lines.Line._
import org.hammerlab.test.matchers.lines.{ Line, NotChar }

class ErrorJSTest
  extends ErrorTest {

  val any = NotChar(')')

  def firstLines: Seq[Line] =
    Seq[Line](
       "java.lang.IllegalArgumentException: error msg",
      l"  at <jscode>.$$c_jl_IllegalArgumentException.$$c_jl_Throwable.fillInStackTrace__jl_Throwable($any)",
      l"  at <jscode>.$$c_jl_IllegalArgumentException.$$c_jl_Throwable.init___T__jl_Throwable__Z__Z($any)",
      l"  at java.lang.IllegalArgumentException.<init>($any)",
      l"  at <jscode>.{anonymous}()($any)"
    )

  def causedBy: Seq[Line] =
    Seq[Line](
       "Caused by:",
      l"  java.lang.NullPointerException",
      l"    at <jscode>.$$c_jl_NullPointerException.$$c_jl_Throwable.fillInStackTrace__jl_Throwable($any)",
      l"    at <jscode>.$$c_jl_NullPointerException.$$c_jl_Throwable.init___T__jl_Throwable__Z__Z($any)",
      l"    at java.lang.NullPointerException.<init>($any)"
    )
}
