
lazy val io_utils = rootProject("io-utils", bytes, channel, io)

addScala212

lazy val bytes = project.settings(
  version := "1.0.2",
  deps ++= Seq(
    args4j,
    args4s % "1.3.0",
    case_app,
    cats
  )
)

lazy val channel = project.settings(
  version := "1.1.0",
  deps ++= Seq(
    math % "2.0.0",
    paths % "1.3.0",
    slf4j
  )
).dependsOn(
  bytes,
  io
)

lazy val io = project.settings(
  version := "1.2.0",
  deps ++= Seq(
    case_app,
    cats,
    paths % "1.2.0",
    slf4j
  )
)
