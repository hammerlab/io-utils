package org.hammerlab.show

import cats.Show
import hammerlab.show._
import shapeless._

trait product {
  implicit val showHNil: Show[HNil] = show { _ ⇒ "" }
  implicit def showHList[H, T <: HList](implicit
                                        head: Show[H],
                                        tail: Show[T]): Show[H :: T] =
    show[H :: T] {
      case h :: HNil ⇒
        h.show
      case h :: t ⇒
        show"$h,$t"
    }

  implicit def showAsHList[T, G <: HList](implicit
                                          generic: Generic.Aux[T, G],
                                          showGeneric: Show[G]
                                         ): Show[T] =
    show {
      t ⇒
        val name =
          t.getClass.getSimpleName match {
            case "" ⇒ "anon"
            case name ⇒ name
          }

        generic.to(t) match {
          case _: HNil ⇒ name.show
          case hl ⇒ show"$name($hl)"
        }

    }
}
