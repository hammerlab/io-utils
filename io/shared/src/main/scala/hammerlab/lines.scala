package hammerlab

import org.hammerlab.lines._

trait limit {
  import org.hammerlab.lines.{ limit ⇒ l }
  type Limit = l.Limit
   val Limit = l.Limit

  type Limited[T] = l.Limited[T]
   val Limited    = l.Limited

  val Unlimited = l.UnLimited

  type Max = l.Max
   val Max = l.Max
}

trait delimiter
  extends Delimiter.instances
     with Serializable {
  import org.hammerlab.{lines ⇒ l}
  type Delimiter = l.Delimiter
}
object delimiter extends delimiter

object lines
  extends HasLines
     with limit
     with Serializable {

  import org.hammerlab.{lines ⇒ l}

  type Indent = l.Indent

  type Line = l.Line
   val Line = l.Line

  type Lines = l.Lines
   val Lines = l.Lines

  type ToLines[T] = l.ToLines[T]
   def ToLines[T] = l.ToLines[T] _

  type Level = l.Level
   val Level = l.Level

   trait generic extends l.generic
  object generic extends generic

  object limit extends limit
}
