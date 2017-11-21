# io

[![Build Status](https://travis-ci.org/hammerlab/io-utils.svg?branch=master)](https://travis-ci.org/hammerlab/io-utils)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/io-utils/badge.svg?branch=master)](https://coveralls.io/github/hammerlab/io-utils?branch=master)
[![org.hammerlab:io_2.* on Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/io_2.11.svg?maxAge=600&label=org.hammerlab:io_2.1[12])](http://search.maven.org/#search%7Cga%7C1%7Corg.hammerlab%20io)

Miscellaneous IO-related abstractions:

## [`show`](src/main/scala/hammerlab/show.scala)

Extensions of [`cats.Show`](https://github.com/typelevel/cats/blob/v0.9.0/core/src/main/scala/cats/Show.scala):

```scala
import hammerlab.show._
```

Exposes:

- `Show` instances for `String`s, `Int`s, and `Long`s
- `.show` syntax
- `Show`-instance derivations for `sealed trait`s and `case class`es


## [`print`](src/main/scala/hammerlab/print.scala)

```scala
import hammerlab.print._
```

### `Printer`
`PrintStream`-like class for printing `Show`-able elements to a file or stdout:

```scala
import hammerlab.path._, hammerlab.indent.tab
implicit val printer = Printer(Path("out.txt"))  // Passing None will write to stdout
print(
  "first line",
  "second line"
)
echo("third line")  // with one argument, need to avoid Predef.print name collision

printer.close
Path("out.txt").read
// first line
// second line
// third line
```

Print collections up to a maximum number of elements:

```scala
implicit val printer = Printer(Path("out.txt"))
implicit val samples = SampleSize(3)
print(
  1 to 10,
  "Numbers:",
  n ⇒ s"First $n numbers:"
)
// First 3 numbers:
// 	1
// 	2
// 	3

print(
  1 to 2,
  "Numbers:",
  n ⇒ s"First $n numbers:"
)
// Numbers:
// 	1
// 	2
```

### `Print`
Declaratively define a `cast.Show` for a class (see [PrintTest.scala](src/test/scala/org/hammerlab/io/PrintTest.scala)):

```scala
:reset  // clear implicit printer above
import hammerlab.print._, hammerlab.show._

case class A(n: Int, s: String)

object A {
  implicit val showA =
    Print[A] {
      new Print(_) {
        /** Unpack argument; implicit [[Printer]] is in scope */
        val A(n, s) = t
        echo(s"$n, $s")
      }
    }
}

A(111, "aaa").show
// "111, aaa\n"
```

## [`timing`](src/main/scala/hammerlab/timing.scala)

Stubs around printing progress messages at intervals:

```scala
import hammerlab.timing._
```

Time a block of code:

```scala
val (durationMS, num) = time { (1 to 1000000 toList) size }
// (21, 1000000)
```

Schedule a "hearbeat" during execution of a block

```scala
var n = 0
var m = 0
heartbeat(
  () ⇒ n += 1
) {
  while (n < 2) {
	println(s"n == $n, m == $m, sleeping…")
	m += 1
	Thread.sleep(600)
  }
  (n, m)
}
// n == 0, m == 0, sleeping…
// n == 0, m == 1, sleeping…
// n == 1, m == 2, sleeping…
// n == 1, m == 3, sleeping…
// (2, 4)
```

## [`exception`](src/main/scala/hammerlab/exception.scala)

Lightly augmented `toString`/`show` for exception stack-traces:

```scala
import hammerlab.exception._
```

