package org.hammerlab.timing

import java.lang.System.currentTimeMillis

trait Timer {
  def time[T](fn: ⇒ T): (Long, T) = {
    val before = currentTimeMillis
    val res = fn
    val after = currentTimeMillis
    (
      after - before,
      res
    )
  }
}

object Timer extends Timer with Serializable
