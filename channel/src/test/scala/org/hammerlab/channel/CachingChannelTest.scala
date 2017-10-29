package org.hammerlab.channel

import java.io.EOFException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import org.hammerlab.channel.CachingChannel._
import org.hammerlab.channel.SeekableByteChannel.ChannelByteChannel
import org.hammerlab.io.Buffer
import org.hammerlab.paths.Path
import org.hammerlab.test.Suite
import org.hammerlab.test.resources.File

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

class MockChannel(path: Path)
  extends ChannelByteChannel(FileChannel.open(path)) {
  val reads = ArrayBuffer[(Long, Int)]()
  override def _read(dst: ByteBuffer): Int = {
    reads += position() → dst.remaining()
    super._read(dst)
  }
}

class CachingChannelTest
  extends Suite {
  test("cache") {
    val ch = new MockChannel(File("test.txt"))
    val buf = Buffer(40)
    val cc =
      ch.cache(
        blockSize = 64,
        maximumSize = 200  // space for 3 blocks; 4th will cause a swap
      )

    ch.reads should be(Nil)

    def seekCheck(
        pos: Int,
        expectedBytes: String
    )(
        expectedBlocks: (Int, Int)*
    )(
        expectedReads: (Int, Int)*
    ): Unit = {
      cc.seek(pos)
      check(
        expectedBytes
      )(
        expectedBlocks: _*
      )(
        expectedReads: _*
      )
    }

    def check(
        expectedBytes: String
    )(
        expectedBlocks: (Int, Int)*
    )(
        expectedReads: (Int, Int)*
    ): Unit = {
      buf.clear()

      cc.readFully(buf)

      buf
        .array()
        .slice(0, 4)
        .map(_.toChar)
        .mkString("") should be(
          expectedBytes
        )

      cc
        .blocks
        .asScala
        .toVector
        .map {
          case (offset, buffer) ⇒
            offset.toInt → buffer.capacity()
        }
        .sortBy(_._1) should be(expectedBlocks)

      // Still just one read from underlying channel
      ch.reads should be(expectedReads)
    }

    // test.txt index:
    //
    // [  0- 10): digits
    // [ 11- 37): lower-case letters
    // [ 38- 64): upper-case letters
    // [ 66- 76): digits, reversed
    // [ 77-103): lower-case letters, reversed
    // [104-130): upper-case letters, reversed
    // [132-182): digits, 5x'd
    // [183-235): lower-case letters, 2x'd
    // [236-288): upper-case letters, 2x'd
    // [290-340): digits, 5x'd + reversed
    // [341-393): lower-case letters, 2x'd + reversed
    // [394-446): upper-case letters, 2x'd + reversed

        check(     "0123" )(0 → 64                )(0 → 64)
    seekCheck(  1, "1234" )(0 → 64                )(0 → 64)
    seekCheck(  1, "1234" )(0 → 64                )(0 → 64)
    seekCheck(  0, "0123" )(0 → 64                )(0 → 64)
    seekCheck(  7, "789\n")(0 → 64                )(0 → 64)
    seekCheck( 48, "KLMN" )(0 → 64, 1 → 64        )(0 → 64, 64 → 64)  // 40-byte buffer runs into 2nd block
    seekCheck( 48, "KLMN" )(0 → 64, 1 → 64        )(0 → 64, 64 → 64)
    seekCheck( 47, "JKLM" )(0 → 64, 1 → 64        )(0 → 64, 64 → 64)
    seekCheck(  2, "2345" )(0 → 64, 1 → 64        )(0 → 64, 64 → 64)
    seekCheck(217, "rrss" )(0 → 64, 3 → 64, 4 → 64)(0 → 64, 64 → 64, 192 → 64, 256 → 64)  //
    seekCheck(345, "xxww" )(4 → 64, 5 → 64, 6 → 63)(0 → 64, 64 → 64, 192 → 64, 256 → 64, 320 → 64, 384 → 63)

    intercept[EOFException] {
      cc.seek(408)
      buf.clear
      cc.readFully(buf)
    }

    // Test a partial read
    cc.seek(408)
    buf.clear
    cc.read(buf) should be(39)
    buf
      .array()
      .slice(0, 4)
      .map(_.toChar)
      .mkString("") should be(
        "SSRR"
      )

    ch.reads should be(Seq(0 → 64,  64 → 64, 192 → 64, 256 → 64, 320 → 64, 384 → 63))
  }
}
