package org.hammerlab.io.print

import hammerlab.print._
import hammerlab.show._
import org.hammerlab.io.print.Lines.Empty
import shapeless._

import scala.reflect.ClassTag

class Name[T](override val toString: String) extends AnyVal

trait LowPriorityName {
  implicit def fromClassTag[T](implicit ct: ClassTag[T]): Name[T] =
    new Name(ct.runtimeClass.getSimpleName)
}
object Name extends LowPriorityName {
  implicit def apply[T](name: String): Name[T] = new Name(name)
}

trait LowPriority {
  /**
   * Default instance for product-types, overridden by more specific instances (e.g. based on [[Show]]s)
   */
  implicit def product[T, L <: HList](implicit
                                      gen: Generic.Aux[T, L],
                                      tLines: Lazy[ToLines[L]],
                                      name: Name[T]): ToLines[T] =
    ToLines {
      t ⇒
        Lines(
          name + "(",
          indent(
            tLines.value(gen.to(t))
          ),
          ")"
        )
    }

  import Lines.LineJoinOps

  implicit def cons[H, T <: HList](implicit
                                   hLines: Lazy[ToLines[H]],
                                   tLines: Lazy[ToLines[T]]): ToLines[H :: T] =
    ToLines {
      case h :: HNil ⇒
        hLines.value(h)
      case h :: t ⇒
        Lines(
          hLines.value(h).append(","),
          tLines.value(t)
        )
    }

  implicit val hnil: ToLines[HNil] = ToLines(_ ⇒ Empty)

  def showSeqTemplate[T: ToLines, U <: Seq[T] : Name]: ToLines[U] =
    ToLines { _.join(",") }

  implicit def showSeq[T: ToLines](implicit name: Name[Seq[T]]): ToLines[Seq[T]] = showSeqTemplate[T, Seq[T]]
}

trait generic extends LowPriority {
  implicit def showVec[T: ToLines](implicit name: Name[Vector[T]]): ToLines[Vector[T]] = showSeqTemplate[T, Vector[T]]
  implicit def showArr[T](implicit
                          lines: ToLines[T],
                          name: Name[ Array[T]]): ToLines[ Array[T]] =
    ToLines { showSeq[T](lines, name.toString).apply(_) }
}

object generic extends generic
