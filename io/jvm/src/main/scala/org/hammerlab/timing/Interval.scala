package org.hammerlab.timing

import Thread.sleep

object Interval {
  def heartbeat[T](fn: () ⇒ Unit,
                   bodyFn: ⇒ T,
                   intervalS: Int = 1): T = {
    val thread =
      new StoppableThread {
        override def run(): Unit = {
          val ms = intervalS * 1000
          sleep(ms)
          while (!stopped) {
            fn()
            sleep(ms)
          }
        }
      }

    thread.start()

    val ret = bodyFn

    thread.end()

    ret
  }
}
