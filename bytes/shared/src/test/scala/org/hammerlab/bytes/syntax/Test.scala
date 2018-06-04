package org.hammerlab.bytes.syntax

import org.hammerlab.Suite

/**
 * This test is in a sub-pkg to get at [[hammerlab.bytes.BytesOps]] without interference from
 * [[org.hammerlab.bytes.BytesWrapper]]
 */
class Test
  extends Suite {
  test("syntax") {
    import hammerlab.bytes._
    ===(32.B ,  B(32))
    ===(32.KB, KB(32))
    ===(32.MB, MB(32))
    ===(32.GB, GB(32))
    ===(32.TB, TB(32))
    ===(32.PB, PB(32))
    ===(32.EB, EB(32))
  }
}
