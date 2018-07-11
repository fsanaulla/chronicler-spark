import sbt.Keys.organization

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  organization := "com.github.fsanaulla",
  publishArtifact in IntegrationTest := false
)

lazy val chroniclerSpark = project
  .in(file("."))

lazy val sparkRdd = project
  .in(file("spark-rdd"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Dependencies.core
  )

lazy val sparkDs = project
  .in(file("spark-ds"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-dataset",
    libraryDependencies ++= Dependencies.ds
  )

lazy val sparkStreaming = project
  .in(file("spark-streaming"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-streaming",
    libraryDependencies ++= Dependencies.streaming
  )

lazy val tests = project
  .in(file("tests"))
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-tests",
    version := "1.0.0",
    libraryDependencies ++= Dependencies.itTesting
  )