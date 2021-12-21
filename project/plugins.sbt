resolvers += "Era7 maven releases" at "https://s3-eu-west-1.amazonaws.com/releases.era7.com"

libraryDependencies += "com.sun.activation" % "javax.activation" % "1.2.0"

addSbtPlugin("com.eed3si9n"      % "sbt-projectmatrix" % "0.8.0")
addSbtPlugin("ch.epfl.scala"     % "sbt-bloop"         % "1.4.11")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype"      % "3.9.7")
addSbtPlugin("com.github.sbt"    % "sbt-pgp"           % "2.1.2")
addSbtPlugin("de.heikoseeberger" % "sbt-header"        % "5.6.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"      % "2.4.3")
