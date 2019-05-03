resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"

addSbtPlugin("org.xerial.sbt"        % "sbt-sonatype"       % "2.3")
addSbtPlugin("com.jsuereth"          % "sbt-pgp"            % "1.1.1")
addSbtPlugin("de.heikoseeberger"     % "sbt-header"         % "5.0.0")
addSbtPlugin("io.get-coursier"       % "sbt-coursier"       % "1.0.3")
addSbtPlugin("ohnosequences"         % "sbt-github-release" % "0.7.1")
addSbtPlugin("com.github.romanowski" % "hoarder"            % "1.0.2")