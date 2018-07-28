package org.hammerlab.lines.limit

import caseapp.core.Error.UnrecognizedValue
import caseapp.core.argparser.ArgParser

class LimitTest
  extends hammerlab.Suite {
  test("parse arg") {
    val parser = implicitly[ArgParser[Limit]]
    ==(
      parser(None, "123"),
      Right(Max(123))
    )
    ==(
      parser(None, "-1"),
      Right(Unlimited)
    )
    ==(
      parser(None, "-"),
      Right(Unlimited)
    )
    ==(
      parser(None, "-2"),
      Left(UnrecognizedValue("-2"))
    )
    ==(
      parser(None, "10z"),
      Left(UnrecognizedValue("10z"))
    )
  }
}
