import sbt._

object Dependencies {
  val chronicler = "com.github.fsanaulla" %% "chronicler-url-http" % Versions.chronicler
  val sparkDeps: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark,
    "org.apache.spark" %% "spark-sql"  % Versions.spark
  ) map (_ % Provided)
}
