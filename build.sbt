
lazy val base = rootProject(bytes, channel, io)

addScala212

lazy val bytes = project.settings(
  name := "bytes",
  version := "1.0.2-SNAPSHOT",
  deps ++= Seq(
    args4j,
    args4s % "1.3.0",
    case_app,
    cats
  )
)

lazy val channel = project.settings(
  name := "channel",
  version := "1.0.0",
  deps ++= Seq(
    hammerlab("bytes") % "1.0.0",
    hammerlab("io") % "1.0.0",
    math % "1.0.0",
    paths % "1.2.0",
    slf4j
  )
)

lazy val io = project.settings(
  name := "io",
  version := "1.1.0-SNAPSHOT",
  deps ++= Seq(
    case_app,
    cats,
    paths % "1.2.0",
    slf4j
  )
)
