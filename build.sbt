
addScala212

lazy val bytes = project.settings(
  version := "1.1.0-SNAPSHOT",
  deps ++= Seq(
    args4j,
    args4s % "1.3.0",
    case_app,
    cats
  )
)

lazy val channel = project.settings(
  version := "1.2.1-SNAPSHOT",
  deps ++= Seq(
    math % "2.1.0",
    paths % "1.4.0",
    slf4j
  ),
  testDeps += log4j
).dependsOn(
  bytes,
  io
)

lazy val io = project.settings(
  version := "3.1.0-SNAPSHOT",
  deps ++= Seq(
    case_app,
    cats,
    paths % "1.4.0",
    shapeless_utils % "1.1.0"
  )
)

lazy val io_utils = rootProject("io-utils", bytes, channel, io)
