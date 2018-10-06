
default(
  versions(
    paths → "1.5.0"
  )
)

lazy val bytes = cross.settings(
  r"1.2.0",
  dep(
    case_app,
    cats
  )
)
lazy val `bytes-x` = bytes.x

lazy val channel = project.settings(
  v"1.5.2",
  dep(
    log4j tests,
    math.utils % "2.2.0",
    paths,
    slf4j
  )
).dependsOn(
  bytes.jvm,
     io.jvm
)

lazy val io =
  cross
    .settings(
      v"5.2.0",
      dep(
        case_app,
        cats,
        iterators       % "2.2.0",
        shapeless_utils % "1.3.0",
        types           % "1.3.0"
      ),
      consoleImport(
        "hammerlab.lines._",
        "hammerlab.print._",
        "hammerlab.show._",
        "hammerlab.indent.tab"
      )
    )
    .jvmSettings(
      dep(paths),
      consoleImport("hammerlab.path._")
    )
lazy val `io-x` = io.x

lazy val markdown = cross.settings(
  r"0.1.0",
  dep(
    hammerlab.math.format % "1.0.0",
    shapeless
  )
)
.dependsOn(
  io
)
lazy val `markdown-x` = markdown.x

lazy val `io-utils` =
  root(
     channel,
    `bytes-x`,
    `io-x`,
    `markdown-x`
  )
