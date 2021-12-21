import sbt._

object Library {

  object Versions {
    val chronicler = "0.7.0"
    val spark      = "2.4.7"
    val scalaTest  = "3.2.10"
    val scalaCheck = "1.14.0"
  }

  val chroniclerCore = "com.github.fsanaulla" %% "chronicler-core-shared"    % Versions.chronicler
  val urlIO          = "com.github.fsanaulla" %% "chronicler-url-io"         % Versions.chronicler
  val urlMng         = "com.github.fsanaulla" %% "chronicler-url-management" % Versions.chronicler
  val macros         = "com.github.fsanaulla" %% "chronicler-macros"         % Versions.chronicler
  val scalaCheck     = "org.scalacheck"       %% "scalacheck"                % Versions.scalaCheck
  val generators     = "com.github.fsanaulla" %% "scalacheck-generators"     % "0.2.0"
  val sparkCore      = "org.apache.spark"     %% "spark-core"                % Versions.spark

  val core: List[ModuleID] = List(
    sparkCore % Provided,
    urlIO
  )

  val sparkSql: sbt.ModuleID = "org.apache.spark" %% "spark-sql" % Versions.spark % Provided
  val sparkStreaming: sbt.ModuleID =
    "org.apache.spark" %% "spark-streaming" % Versions.spark % Provided

  val scalaTest = List(
    "org.scalatest" %% "scalatest-freespec",
    "org.scalatest" %% "scalatest-mustmatchers"
  ).map(_ % Versions.scalaTest)

  val itTesting: List[ModuleID] = List(
    "com.dimafeng" %% "testcontainers-scala" % "0.39.5",
    scalaCheck,
    generators,
    macros,
    sparkCore,
    sparkSql
  ) ++ scalaTest

}
