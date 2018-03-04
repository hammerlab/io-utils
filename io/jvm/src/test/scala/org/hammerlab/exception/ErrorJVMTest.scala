package org.hammerlab.exception

import org.hammerlab.test.matchers.lines.Line
import org.hammerlab.test.matchers.lines.Line._
import org.hammerlab.test.version.Util.is2_12

class ErrorJVMTest
  extends ErrorTest {

  def firstLines: Seq[Line] =
    if (is2_12)
      Seq[Line](
         "java.lang.IllegalArgumentException: error msg",
        l"  at org.hammerlab.exception.ErrorTest.$$anonfun$$new$$1(ErrorTest.scala:$d)",
        l"  at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf.outcomeOf$$(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)"
      )
    else
      Seq[Line](
         "java.lang.IllegalArgumentException: error msg",
        l"  at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"  at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$class.outcomeOf(OutcomeOf.scala:$d)",
        l"  at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)"
      )

  def causedBy: Seq[Line] =
    if (is2_12)
      Seq[Line](
         "Caused by:",
        l"  java.lang.NullPointerException",
        l"    at org.hammerlab.exception.ErrorTest.$$anonfun$$new$$1(ErrorTest.scala:$d)",
        l"    at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf.outcomeOf$$(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.Transformer.apply(Transformer.scala:$d)"
      )
    else
      Seq[Line](
        "Caused by:",
        l"  java.lang.NullPointerException",
        l"    at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"    at org.hammerlab.exception.ErrorTest$$$$anonfun$$1.apply(ErrorTest.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$class.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.OutcomeOf$$.outcomeOf(OutcomeOf.scala:$d)",
        l"    at org.scalatest.Transformer.apply(Transformer.scala:$d)"
      )
}
