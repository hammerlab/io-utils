package org.hammerlab.timing

import hammerlab.timing._
import org.hammerlab.Suite

class IntervalTest
  extends Suite {
  test("basic") {
    var n = 0
    var m = 0
    ==(
      heartbeat(
        () ⇒ n += 1
      ) {
        while (n < 2) {
          println(s"n == $n, m == $m, sleeping…")
          m += 1
          Thread.sleep(600)
        }
        (n, m)
      }, (2, 4)
    )
  }
}
