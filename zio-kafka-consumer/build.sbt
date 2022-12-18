ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "zio-kafka-consumer"
  )

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

libraryDependencies ++= Seq("dev.zio" %% "zio" % "2.0.5",
  "dev.zio" %% "zio-kafka" % "2.0.1",
  "dev.zio" %% "zio-json" % "0.4.2",
  "org.slf4j" % "slf4j-simple" % "2.0.6",
  "com.typesafe" % "config" % "1.4.1"
)