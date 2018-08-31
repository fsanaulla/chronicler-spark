import com.typesafe.sbt.SbtPgp.autoImportImpl.useGpg
import sbt.Keys._
import sbt.librarymanagement.{Developer, LibraryManagementSyntax, ScmInfo}
import sbt.{Opts, url}

object Settings extends LibraryManagementSyntax {

  private val apacheUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"

  private object Owner {
    val id = "fsanaulla"
    val name = "Faiaz Sanaulla"
    val email = "fayaz.sanaulla@gmail.com"
    val github = "https://github.com/fsanaulla"
  }

  val common = Seq(
    scalaVersion := "2.11.8",
    organization := "com.github.fsanaulla",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-encoding", "utf-8"
    ),
    homepage := Some(url("https://github.com/fsanaulla/chronicler-spark")),
    licenses += "Apache-2.0" -> url(apacheUrl),
    developers += Developer(
      id = Owner.id,
      name = Owner.name,
      email = Owner.email,
      url = url(Owner.github)
    ),
    publishArtifact in IntegrationTest := false
  )


  val publish = Seq(
    useGpg := false,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/fsanaulla/chronicler-spark"),
        "scm:git@github.com:fsanaulla/chronicler-spark.git"
      )
    ),
    pomIncludeRepository := (_ => false),
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    )
  )
}