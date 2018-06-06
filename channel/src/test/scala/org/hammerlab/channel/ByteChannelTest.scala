package org.hammerlab.channel

import java.io.{ EOFException, IOException }
import java.nio.ByteBuffer
import java.nio.channels.{ FileChannel, SeekableByteChannel ⇒ SeekableChannel }

import org.hammerlab.channel.CachingChannel._
import org.hammerlab.io.Buffer
import org.hammerlab.test.Suite
import org.hammerlab.test.resources.File

import scala.Array.fill

case class ChannelStub(reads: String*)
  extends SeekableChannel {
  val bytes =
    reads
      .iterator
      .zipWithIndex
      .map {
        case (str, idx) ⇒
          str.length →
            str
              .iterator
              .map(_.toInt)
              .buffered
      }
      .buffered

  var position = 0L
  override def read(dst: ByteBuffer): Int = {
    if (!bytes.hasNext)
      -1
    else {
      val (length, chars) = bytes.head

      val remaining = dst.remaining()
      val arr = dst.array()
      val off = dst.position()

      val taken = chars.take(remaining).toArray
      for (i ← taken.indices) {
        arr(off + i) = taken(i).toByte
      }

      if (!chars.hasNext)
        bytes.next

      dst.position(off + taken.length)
      position += taken.length
      taken.length
    }
  }

  override def isOpen: Boolean = true
  override def close(): Unit = ()

  override def size(): Long = ???
  override def truncate(size: Long): SeekableChannel = ???
  override def position(newPosition: Long): SeekableChannel = ???
  override def write(src: ByteBuffer): Int = ???
}

class ByteChannelTest
  extends Suite {

  val path = File("test.txt").path

  test("incomplete one empty read") {
    val ch: SeekableByteChannel =
      ChannelStub(
        "12345",
        "67890",
        "1",
        "",
        "234"
      )

    val b4 = Buffer(4)

    ch.readFully(b4)
    ==(b4.array.map(_.toChar).mkString(""), "1234")
    ==(ch.position(), 4)

    b4.position(0)
    ch.readFully(b4)
    ==(b4.array.map(_.toChar).mkString(""), "5678")
    ==(ch.position(), 8)

    b4.position(0)
    ch.readFully(b4)
    ==(b4.array.map(_.toChar).mkString(""), "9012")
    ==(ch.position(), 12)

    b4.position(0)
    intercept[EOFException] {
      ch.readFully(b4)
    }

    ==(ch.isOpen, true)
    ch.close()
    ==(ch.isOpen, false)
  }

  test("incomplete consecutive empty reads") {
    val ch: SeekableByteChannel =
      ChannelStub(
        "12345",
        "67890",
        "1",
        "",
        "",
        "234"
      )

    val b4 = Buffer(4)
    val a4 = fill(4)(0.toByte)

    ch.readFully(b4)
    ==(b4.array.map(_.toChar).mkString(""), "1234")
    ==(ch.position(), 4)

    ch.readFully(a4)
    ==(a4.map(_.toChar).mkString(""), "5678")
    ==(ch.position(), 8)

    b4.position(0)
    ==(
      intercept[IOException] {
        ch.readFully(b4)
      }
      .getMessage,
      "Read 0 bytes twice in a row, 3 bytes into reading 4 from position 8"
    )
  }

  test("read single bytes") {
    val ch: SeekableByteChannel =
      ChannelStub(
        "12345",
        "67890",
        "1",
        "",
        "234"
      )

    ==(ch.position(), 0)
    ==(ch.read().toChar, '1')
    ==(ch.position(), 1)
    ==(ch.read().toChar, '2')
    ==(ch.position(), 2)
    ==(ch.read().toChar, '3')
    ==(ch.position(), 3)
  }

  def check(ch: ByteChannel): Unit = {
    ==(ch.position, 0)

    ==(ch.readString(4, includesNull = false), "0123")
    ==(ch.position, 4)

    ==(ch.read.toChar, '4')
    ==(ch.position, 5)

    val bytes = fill(6)(0.toByte)
    ==(ch.read(bytes), 6)
    ==(bytes.map(_.toChar).mkString(""), "56789\n")
    ==(ch.position, 11)

    ==(ch.read.toChar, 'a')
    ==(ch.position, 12)

    val buf = Buffer(7)
    ==(ch.read(buf), 7)
    ==(buf.array().map(_.toChar).mkString(""), "bcdefgh")
    ==(ch.position, 19)

    ch.skip(250)
    ==(ch.position, 269)

    ch.read(bytes, 1, 2)
    ==(bytes.slice(1, 3).map(_.toChar).mkString(""), "QR")
    ==(ch.position, 271)

    buf.clear()
    ==(ch.read(buf), 7)
    ==(buf.array().slice(0, 7).map(_.toChar).mkString(""), "RSSTTUU")
    ==(ch.position, 278)

    ch.skip(169)
    ==(ch.position, 447)

    ==(ch.read, -1)
    ==(ch.read(bytes), -1)
    buf.clear
    buf.limit(0)
    ==(ch.read(buf), 0)
    buf.clear()
    ==(ch.read(buf), -1)
  }

  test("InputStreamByteChannel") {
    check(path.inputStream)
  }

  test("IteratorByteChannel") {
    check(
      path
        .readBytes
        .iterator
    )
  }

  test("ChannelByteChannel") {
    check(FileChannel.open(path): SeekableByteChannel)
  }

  test("channel array read") {
    val ch: SeekableByteChannel = FileChannel.open(path)
    val bytes = fill(4)(0.toByte)
    ch.read(bytes)
    ==(ch.position(), 4)
  }

  test("CachingChannel") {
    val ch = SeekableByteChannel(path)

    val cc = SeekableByteChannel(path).cache

    val expectedBytes = path.readBytes
    for {
      (i, expectedByte) ← 0 until 100 zip expectedBytes
    } {
      ch.seek(i)
      cc.seek(i)

      withClue(s"idx: $i: ") {
        ==(cc.getInt, ch.getInt)
      }

      ch.seek(i)
      cc.seek(i)

      withClue(s"idx: $i: ") {
        ==(cc.read(), expectedByte)
        ==(ch.read(), expectedByte)
      }
    }
  }
}
