ThisBuild / credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("SONATYPE_LOGIN", "default"),
  sys.env.getOrElse("SONATYPE_PASSWORD", "default")
)