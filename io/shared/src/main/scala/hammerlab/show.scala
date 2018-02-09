package hammerlab

import org.hammerlab.show.{ coproduct, product, syntax }

/**
 * Convenient import for [[cats.Show]] implicits
 */
trait show
  extends syntax {
  trait all
    extends syntax
      with coproduct
      with product
  object all extends all
}
object show extends show
