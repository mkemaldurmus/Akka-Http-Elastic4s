ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val akkaHttp = "10.2.9"
val elastic4sV = "7.16.0"
val elasticCoreV = "7.16.0"
val elasticCircleV = "7.1.3"
val circe = "0.14.1"
val akkaHttpCirce = "1.39.2"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sV,
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticCoreV,
  "com.github.pjfanning.elastic4s" % "elastic4s-json-circe_2.13" % elasticCircleV,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirce,
  "io.circe" %% "circe-generic" % circe,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",

)


lazy val root = (project in file("."))
  .settings(
    name := "Insider-Case"
  )
