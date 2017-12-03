package org.hammerlab.exception

import hammerlab.show._
import org.hammerlab.test.{ Suite, firstLinesMatch }
import org.hammerlab.test.matchers.lines.Line._
import org.hammerlab.test.version.Util.is2_12

class ErrorTest
  extends Suite {

  import hammerlab.exception._

  test("show") {
    import hammerlab.indent.implicits.spaces.two
    val actual =
      Error(
        new IllegalArgumentException(
          "error msg",
          new NullPointerException
        )
      )
      .show

    // Slight differences in stack-trace in 2.11 vs 2.12
    if (is2_12) {
      actual should firstLinesMatch(
        "java.lang.IllegalArgumentException: error msg",
        l"  at org.hammerlab.exception.ErrorTest.$$anonfun$$new$$1(ErrorTest.scala:$d)",
        l"  at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf.outcomeOf$$(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)"
      )

      val causedBy = actual.drop(actual.indexOf("Caused by:"))

      causedBy should firstLinesMatch(
        "Caused by:",
        l"  java.lang.NullPointerException",
        l"    at org.hammerlab.exception.ErrorTest.$$anonfun$$new$$1(ErrorTest.scala:$d)",
        l"    at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf.outcomeOf$$(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.Transformer.apply(Transformer.scala:$d)"
      )
    } else {
      actual should firstLinesMatch(
         "java.lang.IllegalArgumentException: error msg",
        l"  at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$class.outcomeOf(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)"
      )

      val causedBy = actual.drop(actual.indexOf("Caused by:"))

      causedBy should firstLinesMatch(
         "Caused by:",
        l"  java.lang.NullPointerException",
        l"    at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$class.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.Transformer.apply(Transformer.scala:$d)"
      )
    }
  }
}
