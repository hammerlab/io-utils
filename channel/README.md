# channel

[![Build Status](https://travis-ci.org/hammerlab/io-utils.svg?branch=master)](https://travis-ci.org/hammerlab/io-utils)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/io-utils/badge.svg?branch=master)](https://coveralls.io/github/hammerlab/io-utils?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/channel_2.11.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Corg.hammerlab%20channel)

Classes for unifying, type-safening, and performance-improvening `java.io.InputStream` and `java.nio.channels.{Seekable,}ByteChannel` abstractions:
 
 ## [`ByteChannel`](src/main/scala/org/hammerlab/channel/ByteChannel.scala)
 
 - Basic, un-`seek`-able implementation of `InputStream` and `ReadableByteChannel` interfaces
 - Supports `position`-querying, `skip`, and `readFully` APIs taking/returning bytes, byte-arrays, and `ByteBuffer`s
 - Constructible from `InputStream`s and `Iterator[Byte]`s, by default
 
## [`SeekableByteChannel`](src/main/scala/org/hammerlab/channel/SeekableByteChannel.scala)

- [`ByteChannel`](#bytechannel) that adds `seek` and `size` methods
- Additionally constructible from `java.nio.channels.SeekableByteChannel`s and `org.hammerlab.paths.Path`s

## [`CachingByteChannel`](src/main/scala/org/hammerlab/channel/CachingChannel.scala)

- [`SeekableByteChannel`](#seekablebytechannel) that keeps an LRU cache of an underlying channel
- Defaults: 64KB blocks, 64MB total size
