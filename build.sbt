name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.6"

lazy val akkaVersion = "2.5.22"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.softwaremill.macwire" %% "macros" % "2.3.2" % "provided",
  "com.softwaremill.macwire" %% "macrosakka" % "2.3.2" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.3.2",
   "com.softwaremill.common" %% "tagging" % "2.2.1",
  "net.debasishg" %% "redisreact" % "0.9",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
