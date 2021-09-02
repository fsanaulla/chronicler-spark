import sbt._

object Library {

  object Versions {
    val chronicler = "0.6.8"
    val spark      = "2.4.7"
    val scalaTest  = "3.2.8"
    val scalaCheck = "1.14.0"
  }

  val chroniclerCore = "com.github.fsanaulla" %% "chronicler-core-shared"    % Versions.chronicler
  val urlIO          = "com.github.fsanaulla" %% "chronicler-url-io"         % Versions.chronicler
  val urlMng         = "com.github.fsanaulla" %% "chronicler-url-management" % Versions.chronicler
  val macros         = "com.github.fsanaulla" %% "chronicler-macros"         % Versions.chronicler
  val scalaCheck     = "org.scalacheck"       %% "scalacheck"                % Versions.scalaCheck
  val generators     = "com.github.fsanaulla" %% "scalacheck-generators"     % "0.2.0"

  val core: List[ModuleID] = List(
    "org.apache.spark" %% "spark-core" % Versions.spark % Provided,
    urlIO
  )

  val scalaTest = List(
    "org.scalatest" %% "scalatest-freespec",
    "org.scalatest" %% "scalatest-mustmatchers"
  ).map(_ % Versions.scalaTest)

  val ds: sbt.ModuleID        = "org.apache.spark" %% "spark-sql"       % Versions.spark % Provided
  val streaming: sbt.ModuleID = "org.apache.spark" %% "spark-streaming" % Versions.spark % Provided

  val itTesting: List[ModuleID] = List(
    "com.dimafeng" %% "testcontainers-scala" % "0.39.5",
    scalaCheck,
    generators,
    macros
  ) ++ scalaTest

}
