package hammerlab

/**
 * Import-friendly API, exposing interesting bits via:
 *
 * {{{
 * import hammerlab.bytes._
 * }}}
 */
object bytes {
  type Bytes = org.hammerlab.bytes.Bytes
  val  Bytes = org.hammerlab.bytes.Bytes

  implicit val BytesOps = org.hammerlab.bytes.BytesWrapper _
  val format = org.hammerlab.bytes.Bytes.format
  type  B = org.hammerlab.bytes. B
  type KB = org.hammerlab.bytes.KB
  type MB = org.hammerlab.bytes.MB
  type GB = org.hammerlab.bytes.GB
  type TB = org.hammerlab.bytes.TB
  type PB = org.hammerlab.bytes.PB
  type EB = org.hammerlab.bytes.EB

  val  B = org.hammerlab.bytes. B
  val KB = org.hammerlab.bytes.KB
  val MB = org.hammerlab.bytes.MB
  val GB = org.hammerlab.bytes.GB
  val TB = org.hammerlab.bytes.TB
  val PB = org.hammerlab.bytes.PB
  val EB = org.hammerlab.bytes.EB

  type BytesOptionHandler = org.hammerlab.bytes.BytesOptionHandler
}
