package hammerlab

import org.hammerlab.timing.Interval

object timing {
  type Timer = org.hammerlab.timing.Timer
  def time[T] = org.hammerlab.timing.Timer.time[T] _

  type StoppableThread = org.hammerlab.timing.StoppableThread

  def heartbeat[T](fn: () ⇒ Unit)(bodyFn: => T): T =
    heartbeat(fn, intervalS = 1)(bodyFn)

  def heartbeat[T](fn: () ⇒ Unit,
                   intervalS: Int)(bodyFn: => T): T =
    heartbeat(
      fn,
      bodyFn,
      intervalS
    )

  def heartbeat[T](fn: () ⇒ Unit,
                   bodyFn: ⇒ T,
                   intervalS: Int = 1): T =
    Interval.heartbeat(
      fn,
      bodyFn,
      intervalS
    )
}
