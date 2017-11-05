package org.hammerlab.timing

import org.hammerlab.test.Suite

class IntervalTest
  extends Suite {
  test("basic") {
    var n = 0
    var m = 0
    Interval.heartbeat(
      () ⇒ n += 1,
      {
        while (n < 2) {
          println(s"n == $n, sleeping…")
          m += 1
          Thread.sleep(600)
        }
      }
    )
    n should be(2)
    m should be(4)
  }
}
