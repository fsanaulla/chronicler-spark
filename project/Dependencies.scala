import sbt._

object Dependencies {
  val chronicler = "com.github.fsanaulla" %% "chronicler-url-http"    % Versions.chronicler
  val macros     = "com.github.fsanaulla" %% "chronicler-macros"      % Versions.chronicler
  val scalaTest  = "org.scalatest"        %% "scalatest"              % Versions.scalaTest
  val scalaCheck = "org.scalacheck"       %% "scalacheck"             % Versions.scalaCheck
  val sparkTests = "com.github.fsanaulla" %% "chronicler-spark-tests" % "1.0.0" // local dependencies
  val generators = "com.github.fsanaulla" %% "scalacheck-generators"  % "0.1.3"

  val core: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark % Provided,
    sparkTests % IntegrationTest,
    scalaTest  % IntegrationTest,
    macros     % IntegrationTest,
    scalaCheck % IntegrationTest,
    generators % IntegrationTest,
    chronicler
  )

  val ds: List[sbt.ModuleID] =
    "org.apache.spark" %% "spark-sql"  % Versions.spark % Provided :: core

  val streaming: List[sbt.ModuleID] =
    "org.apache.spark" %% "spark-streaming" % Versions.spark % Provided :: core

  val itTesting: Seq[ModuleID] = Seq(
    "org.jetbrains"        %  "annotations" % "15.0", // to solve evicted warning
    "org.testcontainers"   %  "influxdb"    % "1.7.3" exclude("org.jetbrains", "annotations"),
    scalaCheck % Provided,
    scalaTest  % Provided,
    generators % Provided,
    macros     % Provided
  )

}
