import sbt._

object Library {

  object Versions {
    val chronicler = "0.6.5"
    val spark      = "2.4.7"
    val scalaTest  = "3.0.8"
    val scalaCheck = "1.14.0"
  }

  val chroniclerCore = "com.github.fsanaulla" %% "chronicler-core-shared"    % Versions.chronicler
  val urlIO          = "com.github.fsanaulla" %% "chronicler-url-io"         % Versions.chronicler
  val urlMng         = "com.github.fsanaulla" %% "chronicler-url-management" % Versions.chronicler
  val macros         = "com.github.fsanaulla" %% "chronicler-macros"         % Versions.chronicler
  val scalaTest      = "org.scalatest"        %% "scalatest"                 % Versions.scalaTest
  val scalaCheck     = "org.scalacheck"       %% "scalacheck"                % Versions.scalaCheck
  val generators     = "com.github.fsanaulla" %% "scalacheck-generators"     % "0.2.0"

  val core: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark % Provided,
    urlIO
  )

  val ds: sbt.ModuleID        = "org.apache.spark" %% "spark-sql"       % Versions.spark % Provided
  val streaming: sbt.ModuleID = "org.apache.spark" %% "spark-streaming" % Versions.spark % Provided

  val itTesting: List[ModuleID] = List(
    "org.jetbrains"      % "annotations" % "15.0", // to solve evicted warning
    "org.testcontainers" % "influxdb"    % "1.7.3" exclude ("org.jetbrains", "annotations"),
    scalaCheck,
    scalaTest,
    generators,
    macros
  )

}
