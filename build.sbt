
default(
  versions(
    paths → "1.5.0"
  ),
  sonatypeStage(1457)
)

lazy val bytes = crossProject.settings(
  v"1.2.0",
  dep(
    case_app,
    cats
  )
)
lazy val bytesJS  = bytes.js
lazy val bytesJVM = bytes.jvm

lazy val channel = project.settings(
  v"1.4.0",
  dep(
    log4j tests,
     math.utils % "2.2.0",
    paths,
    slf4j
  )
).dependsOn(
  bytesJVM,
     ioJVM
)

lazy val io = crossProject.settings(
  v"5.0.1",
  dep(
    case_app,
    cats,
    iterators       % "2.1.0",
    shapeless_utils % "1.2.0",
    types           % "1.1.0"
  ),
  consoleImport(
    "hammerlab.lines._",
    "hammerlab.print._",
    "hammerlab.show._",
    "hammerlab.indent.tab"
  )
)
lazy val ioJS  = io.js
lazy val ioJVM = io.jvm.settings(
  dep(paths),
  consoleImport("hammerlab.path._")
)

lazy val `io-utils` =
  rootProject(
    "io-utils",
    channel,
    bytesJVM, bytesJS,
       ioJVM,    ioJS
  )
