# bytes

[![Build Status](https://travis-ci.org/hammerlab/io-utils.svg?branch=master)](https://travis-ci.org/hammerlab/io-utils)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/io-utils/badge.svg?branch=master)](https://coveralls.io/github/hammerlab/io-utils?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/bytes_2.11.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Corg.hammerlab%20bytes)

Shorthands, syntaxes, and parsers for working with byte amounts.

```scala
import org.hammerlab.bytes._

val size1 = 16 MB
val size2 = Bytes("32m")

size1 < size2  // true
size1.bytes    // 16777216
```

See [Bytes.scala](src/main/scala/org/hammerlab/bytes/Bytes.scala) and [BytesTest.scala](src/test/scala/org/hammerlab/bytes/BytesTest.scala) for more info.
