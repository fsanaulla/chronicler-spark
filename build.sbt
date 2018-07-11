import sbt.Keys.organization

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  organization := "com.github.fsanaulla"
)
lazy val sparkRdd = project
  .in(file("spark-rdd"))
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Dependencies.rdd
  )

lazy val sparkDf = project
  .in(file("spark-ds"))
  .settings(commonSettings: _*)
  .settings(
    name := "chronicler-spark-dataset",
    libraryDependencies ++= Dependencies.df
  )

lazy val sparkStreaming = project
  .in(file("spark-streaming"))
  .settings(
    name := "chronicler-spark-streaming"
  )