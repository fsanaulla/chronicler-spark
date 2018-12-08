import de.heikoseeberger.sbtheader.License

lazy val headerSettings = headerLicense := Some(License.ALv2("2018", "Faiaz Sanaulla"))

lazy val sparkRdd = project
  .in(file("spark-rdd"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Seq(
      Dependencies.arm,
      Dependencies.urlMng % Test
    ) ++ Dependencies.core
  )
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkDs = project
  .in(file("spark-ds"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-ds",
    libraryDependencies ++= Seq(
      Dependencies.ds, 
      Dependencies.urlMng % Test
    )
  )
  .dependsOn(sparkRdd)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStreaming = project
  .in(file("spark-streaming"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-streaming",
    libraryDependencies ++= Seq(
      Dependencies.streaming,
      Dependencies.urlMng     % Test
    )
  )
  .dependsOn(sparkRdd)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStructuredStreaming = project
  .in(file("spark-structured-streaming"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-structured-streaming",
    libraryDependencies ++= Seq(
      Dependencies.ds,
      Dependencies.urlMng % Test
    ) ++ Dependencies.core 
  )
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val tests = project
  .in(file("testing"))
  .settings(Settings.common: _*)
  .settings(
    name := "chronicler-spark-testing",
    libraryDependencies ++= Dependencies.itTesting
  )