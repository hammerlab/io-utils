package org.hammerlab.lines

import hammerlab.delimiter.comma
import hammerlab.iterator._
import hammerlab.lines._
import hammerlab.show._
import shapeless._

import scala.collection.immutable.Stream.Empty
import scala.collection.mutable
import scala.reflect.ClassTag

trait sealedtrait {
  implicit def traitToLines[T, C <: Coproduct](implicit
                                              gen: Generic.Aux[T, C],
                                              tLines: Lazy[ToLines[C]]): ToLines[T] =
    ToLines { t ⇒ tLines.value(gen.to(t)) }

  implicit def ccons[H, T <: Coproduct](implicit
                                        hLines: Lazy[ToLines[H]],
                                        tLines: Lazy[ToLines[T]]): ToLines[H :+: T] =
    ToLines {
      case Inl(h) ⇒ hLines.value(h)
      case Inr(t) ⇒ tLines.value(t)
    }

  implicit val cnil: ToLines[CNil] = ToLines { _ ⇒ ??? }
}

trait caseclass {

  val tupleRegex = """scala\.Tuple\d+""".r

  /**
   * Default [[ToLines]] instance for product-types, overridden by more specific instances (e.g. based on [[Show]]s)
   */
  implicit def product[T, L <: HList](implicit
                                      gen: Generic.Aux[T, L],
                                      tLines: Lazy[ToLines[L]],
                                      name: Name[T],
                                      ct: ClassTag[T]): ToLines[T] =
    ToLines {
      t ⇒
        /* Remove name for [[scala.Tuple]]s */
        val n =
          ct.runtimeClass.getName match {
            case tupleRegex() ⇒ ""
            case _ ⇒ name.toString
          }
        gen.to(t) match {
          case l: HNil ⇒ n
          case l ⇒
            tLines.value(l) match {
              case Lines(Empty) ⇒ s"$n()"
              case Lines(line @ Line(str, level) #:: Empty) ⇒
                Line(
                  s"$n($str)",
                  level
                )
              case lines ⇒
                Lines(
                  n + "(",
                  indent(lines),
                  ")"
                )
            }
        }
    }

  /**
   * Extend an [[HList]]'s [[ToLines]] instance
   */
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

  implicit val hnil: ToLines[HNil] = ToLines(_ ⇒ Lines.empty)

}

trait generic
  extends sealedtrait
     with caseclass {

  implicit def seqLines[T, U[_] <: Iterable[_]](implicit
                                                lines: Lazy[ToLines[T]],
                                                ev: U[T] <:< Iterable[T],
                                                name: Name[U[T]],
                                                delimiter: Delimiter = comma): ToLines[U[T]] =
    ToLines {
        case Seq() ⇒
          s"$name()"
        case elems ⇒
          implicit val l: ToLines[T] = lines.value
          Lines(
            s"$name(",
            indent(
              elems.join(delimiter)
            ),
            ")"
          )
      }

  implicit def arrayLines[T](implicit
                             lines: Lazy[ToLines[T]],
                             name: Name[Array[T]],
                             delimiter: Delimiter = comma): ToLines[Array[T]] =
    ToLines {
      arr ⇒
        implicit val l: ToLines[T] = lines.value
        implicit val nm: Name[mutable.ArraySeq[T]] = name.toString
        seqLines[
          T,
          mutable.ArraySeq
        ]
        .apply(
          mutable.ArraySeq(arr: _*)
        )
    }
}

object generic extends generic
