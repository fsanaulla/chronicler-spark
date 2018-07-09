import sbt._

object Dependencies {
  val chronicler = "com.github.fsanaulla" %% "chronicler-url-http" % Versions.chronicler

  val rdd: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark % Provided,
    chronicler
  )
}
