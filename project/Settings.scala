import com.jsuereth.sbtpgp.SbtPgp.autoImport._
import sbt.Keys._
import sbt._
import sbt.librarymanagement.{Developer, LibraryManagementSyntax, ScmInfo}
import xerial.sbt.Sonatype.autoImport._

object Settings extends LibraryManagementSyntax {
  private val apacheUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"
}
