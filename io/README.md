# io

[![Build Status](https://travis-ci.org/hammerlab/io-utils.svg?branch=master)](https://travis-ci.org/hammerlab/io-utils)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/io-utils/badge.svg?branch=master)](https://coveralls.io/github/hammerlab/io-utils?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/io_2.11.svg?maxAge=600)](http://search.maven.org/#search%7Cga%7C1%7Corg.hammerlab%20io)

Miscellaneous IO-related abstractions:

- [`Show`](src/main/scala/org/hammerlab/io/Show.scala): wrapper for [`cats.Show`](https://github.com/typelevel/cats/blob/v0.9.0/core/src/main/scala/cats/Show.scala) that automatically accepts `String`s
- [`Printer`](src/main/scala/org/hammerlab/io/Printer.scala): `PrintStream`-like class for printing `Show`-able elements to a file or stdout
- [`timing`](src/main/scala/org/hammerlab/timing), [`exception`](src/main/scala/org/hammerlab/exception): stubs around printing progress messages at intervals and parsing stack-traces, resp.

