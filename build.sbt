import de.heikoseeberger.sbtheader.License
import xerial.sbt.Sonatype._

lazy val headerSettings = headerLicense := Some(License.ALv2("2018-2021", "Faiaz Sanaulla"))

val scala212 = "2.12.14"
val scala211 = "2.11.12"

ThisBuild / scalaVersion := scala212
ThisBuild / organization := "com.github.fsanaulla"
ThisBuild / description := "InfluxDB connector to Apache Spark on top of Chronicler "
ThisBuild / homepage := Some(url(s"${Owner.github}/${Owner.projectName}"))
ThisBuild / developers += Developer(
  id = Owner.id,
  name = Owner.name,
  email = Owner.email,
  url = url(Owner.github)
)

// publish
ThisBuild / scmInfo := Some(
  ScmInfo(
    url(s"${Owner.github}/${Owner.projectName}"),
    s"scm:git@github.com:${Owner.id}/${Owner.projectName}.git"
  )
)
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / "target" / "sonatype-staging" / s"${version.value}"
ThisBuild / sonatypeProjectHosting := Some(
  GitHubHosting(Owner.github, Owner.projectName, Owner.email)
)
ThisBuild / licenses := Seq(
  "APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray)
ThisBuild / publishMavenStyle := true

lazy val `chronicler-spark` = project
  .in(file("."))
  .settings(
    publish / skip := true
  )
  .configure(license)
  .aggregate(
    Seq(core, sparkRdd, sparkDs, sparkStreaming, sparkStructuredStreaming, testing)
      .flatMap(_.projectRefs): _*
  )

lazy val core = projectMatrix
  .in(file("modules/core"))
  .settings(headerSettings)
  .settings(
    name := "chronicler-spark-core",
    libraryDependencies += Library.chroniclerCore
  )
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkRdd = projectMatrix
  .in(file("modules/rdd"))
  .settings(headerSettings)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Seq(
      Library.urlMng % Test
    ) ++ Library.core
  )
  .dependsOn(core)
  .dependsOn(testing % "test->test")
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkDs = projectMatrix
  .in(file("modules/ds"))
  .settings(headerSettings)
  .settings(
    name := "chronicler-spark-ds",
    libraryDependencies ++= Seq(
      Library.ds,
      Library.urlMng % Test
    )
  )
  .dependsOn(sparkRdd)
  .dependsOn(testing % "test->test")
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStreaming = projectMatrix
  .in(file("modules/streaming"))
  .settings(headerSettings)
  .settings(
    name := "chronicler-spark-streaming",
    libraryDependencies ++= Seq(
      Library.streaming,
      Library.urlMng % Test
    ),
    Test / parallelExecution := false
  )
  .dependsOn(sparkRdd)
  .dependsOn(testing % "test->test")
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStructuredStreaming = projectMatrix
  .in(file("modules/structured-streaming"))
  .settings(headerSettings)
  .settings(
    name := "chronicler-spark-structured-streaming",
    libraryDependencies ++= Seq(
      Library.ds,
      Library.urlMng % Test
    ) ++ Library.core
  )
  .dependsOn(sparkRdd)
  .dependsOn(testing % "test->test")
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))
  .enablePlugins(AutomateHeaderPlugin)

lazy val testing = projectMatrix
  .in(file("modules/testing"))
  .settings(
    name := "chronicler-spark-testing",
    libraryDependencies ++= Library.itTesting
  )
  .settings(publish / skip := true)
  .jvmPlatform(scalaVersions = Seq(scala211, scala212))

def license: Project => Project =
  _.settings(
    startYear := Some(2021),
    headerLicense := Some(HeaderLicense.ALv2("2021", Owner.name))
  ).enablePlugins(AutomateHeaderPlugin)
