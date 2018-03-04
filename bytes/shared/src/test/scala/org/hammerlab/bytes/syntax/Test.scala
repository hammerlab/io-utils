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
    32.B  should be( B(32))
    32.KB should be(KB(32))
    32.MB should be(MB(32))
    32.GB should be(GB(32))
    32.TB should be(TB(32))
    32.PB should be(PB(32))
    32.EB should be(EB(32))
  }
}
