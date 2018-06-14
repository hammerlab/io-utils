
default(
  versions(
    paths → "1.5.0"
  )
)

lazy val bytes = crossProject.settings(
  r"1.2.0",
  dep(
    case_app,
    cats
  )
)
lazy val `bytes.js`  = bytes.js
lazy val `bytes.jvm` = bytes.jvm
lazy val `bytes-x`   = parent(`bytes.js`, `bytes.jvm`)

lazy val channel = project.settings(
  v"1.5.1",
  dep(
    log4j tests,
    math.utils % "2.2.0",
    paths,
    slf4j
  )
).dependsOn(
  `bytes.jvm`,
     `io.jvm`
)

lazy val io = crossProject.settings(
  v"5.1.1",
  dep(
    case_app,
    cats,
    iterators       % "2.2.0",
    shapeless_utils % "1.3.0",
    types           % "1.2.0"
  ),
  consoleImport(
    "hammerlab.lines._",
    "hammerlab.print._",
    "hammerlab.show._",
    "hammerlab.indent.tab"
  )
)
lazy val `io.js`  = io.js
lazy val `io.jvm` = io.jvm.settings(
  dep(paths),
  consoleImport("hammerlab.path._")
)
lazy val `io-x` = parent(`io.js`, `io.jvm`)

lazy val `io-utils` =
  root(
    channel,
    `bytes-x`,
    `io-x`
  )
