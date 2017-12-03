package hammerlab

import org.hammerlab.io
import org.hammerlab.io.indent.instances

object indent
  extends instances {
  object implicits extends io.indent.implicits
  type Indent = io.indent.Indent
  type ToLines[T] = io.indent.ToLines[T]
}
