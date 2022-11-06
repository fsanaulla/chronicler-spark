resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"

libraryDependencies += "com.sun.activation" % "javax.activation" % "1.2.0"

addSbtPlugin("com.eed3si9n"      % "sbt-projectmatrix" % "0.9.0")
addSbtPlugin("ch.epfl.scala"     % "sbt-bloop"         % "1032048a")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype"      % "3.9.13")
addSbtPlugin("com.github.sbt"    % "sbt-pgp"           % "2.2.0")
addSbtPlugin("de.heikoseeberger" % "sbt-header"        % "5.7.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"      % "2.4.6")
