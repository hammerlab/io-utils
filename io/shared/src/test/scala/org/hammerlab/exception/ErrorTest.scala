package org.hammerlab.exception

import hammerlab.indent.spaces
import hammerlab.lines._
import org.hammerlab.Suite
import org.hammerlab.test.matchers.lines.Line

abstract class ErrorTest
  extends Suite {

  def firstLines: Seq[Line]
  def   causedBy: Seq[Line]

  test("show") {
    val actual =
      Error(
        new IllegalArgumentException(
          "error msg",
          new NullPointerException
        )
      )
      .showLines

    actual should firstLinesMatch(firstLines: _*)

    val causedByStack = actual.drop(actual.indexOf("Caused by:"))

    causedByStack should firstLinesMatch(causedBy: _*)
  }
}
