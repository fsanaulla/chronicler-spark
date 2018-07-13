import de.heikoseeberger.sbtheader.License

lazy val headerSettings = headerLicense := Some(License.ALv2("2018", "Faiaz Sanaulla"))

lazy val sparkRdd = project
  .in(file("spark-rdd"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Dependencies.core
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkDs = project
  .in(file("spark-ds"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-ds",
    libraryDependencies ++= Dependencies.ds
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStreaming = project
  .in(file("spark-streaming"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-streaming",
    libraryDependencies ++= Dependencies.streaming
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val tests = project
  .in(file("tests"))
  .settings(Settings.common: _*)
  .settings(
    name := "chronicler-spark-tests",
    version := "1.0.0",
    libraryDependencies ++= Dependencies.itTesting
  )