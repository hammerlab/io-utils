
addScala212

lazy val bytes = project.settings(
  r"1.1.0",
  dep(
    args4j,
    args4s % "1.3.0",
    case_app,
    cats
  )
)

lazy val channel = project.settings(
  r"1.3.0",
  dep(
    log4j tests,
     math % "2.1.2",
    paths % "1.4.0",
    slf4j
  )
).dependsOn(
  bytes,
  io
)

lazy val io = project.settings(
  r"4.0.0",
  dep(
    case_app,
    cats,
    paths           % "1.4.0",
    shapeless_utils % "1.1.0",
    types           % "1.0.1"
  ),
  consoleImport(
    "hammerlab.path._",
    "hammerlab.print._",
    "hammerlab.show._",
    "hammerlab.indent.implicits.tab"
  )
)

lazy val io_utils = rootProject("io-utils", bytes, channel, io)
