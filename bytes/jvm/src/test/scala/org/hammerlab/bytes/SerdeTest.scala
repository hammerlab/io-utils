package org.hammerlab.bytes

import java.io._

import org.hammerlab.Suite

class SerdeTest
  extends Suite {
  test("serde") {
    val baos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(2.MB)
    oos.close()

    val bais = new ByteArrayInputStream(baos.toByteArray)
    val ois = new ObjectInputStream(bais)
    ois.readObject().asInstanceOf[Bytes] should be(2.MB)
  }
}
