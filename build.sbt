name := "http4s-proxy-demo"
scalaVersion := "2.11.8"

val http4sVersion = "0.15.0a"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,

  "net.ruippeixotog" %% "scala-scraper" % "1.1.0",
  "org.log4s" %% "log4s" % "1.3.3",
  "org.slf4j" % "slf4j-simple" % "1.7.21"
)
