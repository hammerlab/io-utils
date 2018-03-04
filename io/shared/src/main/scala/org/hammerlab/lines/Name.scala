package org.hammerlab.lines

import scala.reflect.ClassTag

/**
 * Type-class for controlling the printed names of classes in generic printing below.
 *
 * Defaults to class' "simple name", from [[ClassTag]]
 */
class Name[T](override val toString: String) extends AnyVal

object Name {
  implicit def apply[T](name: String): Name[T] = new Name(name)

  implicit def fromClassTag[T](implicit ct: ClassTag[T]): Name[T] =
    new Name(
      ct
        .runtimeClass
        .getSimpleName
        .replaceAll("\\$$", "")
    )
}
