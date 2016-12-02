name := "http4s-proxy-demo"
scalaVersion := "2.12.0"

val http4sVersion = "0.15.0a"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.slf4j" % "slf4j-simple" % "1.7.21"
)
