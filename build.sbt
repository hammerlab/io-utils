
default(
  versions(
    paths → "1.5.0".snapshot
  )
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
     math % "2.1.2",
    paths,
    slf4j
  )
).dependsOn(
  bytesJVM,
     ioJVM
)

lazy val io = crossProject.settings(
  v"4.1.0",
  dep(
    case_app,
    cats,
    iterators       % "2.1.0".snapshot,
    shapeless_utils % "1.2.0".snapshot,
    types           % "1.0.2".snapshot
  ),
  consoleImport(
    "hammerlab.print._",
    "hammerlab.show._",
    "hammerlab.indent.implicits.tab"
  )
)
lazy val ioJS  = io.js
lazy val ioJVM = io.jvm.settings(
  dep(
    paths
  ),
  consoleImport(
    "hammerlab.path._"
  )
)

lazy val parallel = project.settings(
  v"1.0.0",
  publishTestJar
).dependsOn(
  ioJVM
)

lazy val io_utils =
  rootProject(
    "io-utils",
    channel,
    parallel,
    bytesJVM, bytesJS,
       ioJVM,    ioJS
  )
