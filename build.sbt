
lazy val bytes = project.settings(
  name := "bytes",
  version := "1.0.0-SNAPSHOT",
  deps ++= Seq(
    args4j,
    args4s % "1.2.4-SNAPSHOT",
    case_app,
    cats
  )
)

lazy val io = project.settings(
  name := "io",
  version := "1.0.0-SNAPSHOT",
  deps ++= Seq(
    case_app,
    cats,
    paths % "1.1.1-SNAPSHOT"
  )
)
