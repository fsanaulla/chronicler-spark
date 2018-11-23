import sbt._

object Dependencies {
  
  object Versions {
    val chronicler = "0.4.1"
    val spark      = "2.4.0"
    val scalaTest  = "3.0.5"
    val scalaCheck = "1.14.0"
  }
  
  val urlIO      = "com.github.fsanaulla" %% "chronicler-url-io"         % Versions.chronicler
  val urlMng     = "com.github.fsanaulla" %% "chronicler-url-management" % Versions.chronicler
  val macros     = "com.github.fsanaulla" %% "chronicler-macros"         % Versions.chronicler
  val scalaTest  = "org.scalatest"        %% "scalatest"                 % Versions.scalaTest
  val scalaCheck = "org.scalacheck"       %% "scalacheck"                % Versions.scalaCheck
  val arm        = "com.jsuereth"         %% "scala-arm"                 % "2.0"
  val generators = "com.github.fsanaulla" %% "scalacheck-generators"     % "0.2.0"

  val core: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark % Provided,
    scalaTest  % Test,
    macros     % Test,
    scalaCheck % Test,
    generators % Test,
    urlMng     % Test,
    urlIO
  )

  val ds: List[sbt.ModuleID] =
    "org.apache.spark" %% "spark-sql" % Versions.spark % Provided :: core

  val streaming: List[sbt.ModuleID] =
    "org.apache.spark" %% "spark-streaming" % Versions.spark % Provided :: core

  val itTesting: Seq[ModuleID] = Seq(
    "org.jetbrains"        %  "annotations" % "15.0", // to solve evicted warning
    "org.testcontainers"   %  "influxdb"    % "1.7.3" exclude("org.jetbrains", "annotations"),
    scalaCheck,
    scalaTest,
    generators,
    macros
  )

}
