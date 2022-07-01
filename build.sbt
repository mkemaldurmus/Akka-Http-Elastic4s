name := "insider-case"

version := "1.21.1"

scalaVersion := "2.13.8"

val akkaHttp = "10.2.9"
val elastic4sV = "7.16.0"
val elasticCoreV = "7.16.0"
val elasticCircleV = "7.1.3"
val circe = "0.14.1"
val akkaHttpCirce = "1.39.2"
val awsV = "1.12.141"
val akkaV = "2.6.9"
val quillV = "3.12.0"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirce,
  "com.amazonaws" % "aws-java-sdk-s3" % awsV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % elastic4sV,
  "io.circe" %% "circe-generic" % circe,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sV,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elasticCoreV,
  "io.getquill" %% "quill-async-postgres" % quillV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test"
)

