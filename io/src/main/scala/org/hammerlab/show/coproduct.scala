package org.hammerlab.show

import cats.Show
import hammerlab.show._
import shapeless._

trait coproduct {
  implicit val showCNil: Show[CNil] = show { _ ⇒ "" }

  implicit def showCoproduct[H, T <: Coproduct](implicit
                                                head: Show[H],
                                                tail: Show[T]): Show[H :+: T] =
    show[H :+: T] {
      case Inl(h) ⇒
        h.show
      case Inr(t) ⇒
        t.show
    }

  implicit def showAsCoproduct[T, G <: Coproduct](implicit
                                                  generic: Generic.Aux[T, G],
                                                  showGeneric: Show[G]
                                                 ): Show[T] =
    show[T] {
      generic
        .to(_)
        .show
    }
}
