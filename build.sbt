name := "Http4sExample"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.5.0",
  "org.typelevel" %% "cats-effect" % "1.1.0",
  "io.chrisdavenport" %% "cats-par" % "0.2.0",

  "co.fs2" %% "fs2-core" % "1.0.0",
  "co.fs2" %% "fs2-io" % "1.0.0",

  "org.http4s" %% "http4s-dsl" % "0.20.0-M5",
  "org.http4s" %% "http4s-blaze-server" % "0.20.0-M5",
  "org.http4s" %% "http4s-blaze-client" % "0.20.0-M5",
  "org.http4s" %% "http4s-circe" % "0.20.0-M5",
  "org.http4s" %% "http4s-dsl" % "0.20.0-M5",

  "io.circe" %% "circe-core" % "0.10.1",
  "io.circe" %% "circe-generic" % "0.10.1",

  "io.chrisdavenport" %% "log4cats-slf4j" % "0.2.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.tpolecat" %% "doobie-core" % "0.6.0",
  "org.tpolecat" %% "doobie-hikari" % "0.6.0",
  "org.tpolecat" %% "doobie-postgres" % "0.6.0",

  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "io.chrisdavenport" %% "log4cats-noop" % "0.2.0" % "test"
)

addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.8")
addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")