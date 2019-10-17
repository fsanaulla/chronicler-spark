import com.jsuereth.sbtpgp.SbtPgp.autoImport._
import sbt.Keys._
import sbt.librarymanagement.{Developer, LibraryManagementSyntax, ScmInfo}
import sbt.{Opts, file, url}

object Settings extends LibraryManagementSyntax {

  private val apacheUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"

  private object Owner {
    val id = "fsanaulla"
    val name = "Faiaz Sanaulla"
    val email = "fayaz.sanaulla@gmail.com"
    val github = "https://github.com/fsanaulla"
  }

  val common = Seq(
    scalaVersion := "2.12.10",
    organization := "com.github.fsanaulla",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-encoding", "utf-8"
    ),
    crossScalaVersions := Seq("2.11.12", scalaVersion.value),
    homepage := Some(url("https://github.com/fsanaulla/chronicler-spark")),
    licenses += "Apache-2.0" -> url(apacheUrl),
    developers += Developer(
      id = Owner.id,
      name = Owner.name,
      email = Owner.email,
      url = url(Owner.github)
    )
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
    ),
    pgpPublicRing := file("pubring.asc"),
    pgpSecretRing := file("secring.asc"),
    pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray)
  )
}
