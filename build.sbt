
lazy val base =
  project
    .in(file("."))
    .settings(
      publish := { },
      test := { }
    )
    .aggregate(bytes, io)

lazy val bytes = project.settings(
  name := "bytes",
  version := "1.0.0",
  deps ++= Seq(
    args4j,
    args4s % "1.2.4",
    case_app,
    cats
  )
)

lazy val io = project.settings(
  name := "io",
  version := "1.0.0",
  deps ++= Seq(
    case_app,
    cats,
    paths % "1.2.0",
    slf4j
  )
)
