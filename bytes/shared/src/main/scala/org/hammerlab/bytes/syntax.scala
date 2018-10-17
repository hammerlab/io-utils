package org.hammerlab.bytes

trait syntax {
  @inline implicit def makeBytesOps(value: Int): BytesWrapper = BytesWrapper(value)
}
