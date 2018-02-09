package org.hammerlab.show

import hammerlab.show._
import shapeless._

trait product {
  implicit val showHNil: Show[HNil] = Show { _ ⇒ "" }
  implicit def showHList[H, T <: HList](implicit
                                        head: Show[H],
                                        tail: Show[T]): Show[H :: T] =
    Show {
      case h :: HNil ⇒
        h.show
      case h :: t ⇒
        show"$h,$t"
    }

  implicit def showAsHList[T, G <: HList](implicit
                                          generic: Generic.Aux[T, G],
                                          showGeneric: Show[G]
                                         ): Show[T] =
    Show {
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
