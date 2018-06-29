import sbt._
import sbt.Keys._

lazy val root = (project in file("."))
  .settings(
    name := "DetectHacker",
    commonSettings,
    assemblySettings,
    assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
    libraryDependencies ++= commonDependencies ++ Seq(
      "org.apache.flink" %% "flink-scala" % "1.4.0",
      "org.apache.flink" %% "flink-streaming-scala" % "1.4.0",
      "org.apache.flink" %% "flink-connector-kafka-0.11" % "1.4.0"
    )
  ).dependsOn(utils % "test->test;compile->compile")

lazy val utils = (project in file("utils"))
  .settings(
    name := "utils",
    commonSettings,
    libraryDependencies ++= commonDependencies
  )

lazy val simulation = (project in file("simulation"))
  .settings(
    name := "simulation",
    commonSettings,
    assemblySettings,
    mainClass in assembly := Some("Simulation")
  ).dependsOn(utils)


lazy val commonSettings = Seq(
  organization := "org.dizhang",
  version := "0.1",
  scalaVersion := "2.11.12",
  scalacOptions ++= compilerSettings,
  resolvers += Resolver.bintrayRepo("ovotech", "maven")
)

lazy val compilerSettings = Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps"
)


lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",
  "com.github.pureconfig" %% "pureconfig" % "0.9.1",
  "org.apache.kafka" %% "kafka" % "1.1.0"
) ++ {
  val kafkaSerializationV = "0.1.23" // see the Maven badge above for the latest version
  Seq(
    "com.ovoenergy" %% "kafka-serialization-core" % kafkaSerializationV,
    "com.ovoenergy" %% "kafka-serialization-circe" % kafkaSerializationV, // To provide Circe JSON support
    "com.ovoenergy" %% "kafka-serialization-json4s" % kafkaSerializationV // To provide Json4s JSON support
  )} ++ {
  val circeVersion = "0.9.3"
  Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)
}


lazy val assemblySettings = Seq(
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case "reference.conf"                            => MergeStrategy.concat
    case "pom.xml"                                => MergeStrategy.discard
    case "pom.properties"                                => MergeStrategy.discard
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  }
)