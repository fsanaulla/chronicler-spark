import de.heikoseeberger.sbtheader.License

lazy val headerSettings = headerLicense := Some(License.ALv2("2018-2019", "Faiaz Sanaulla"))

lazy val `chronicler-spark` = project
  .in(file("."))
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(skip in publish := true)
  .aggregate(
    sparkCore,
    sparkRdd,
    sparkDs,
    sparkStreaming,
    sparkStructuredStreaming
  )

lazy val sparkCore = project
  .in(file("modules/core"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-core",
    libraryDependencies += Library.chroniclerCore
  )
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkRdd = project
  .in(file("modules/rdd"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-rdd",
    libraryDependencies ++= Seq(
      Library.urlMng % Test
    ) ++ Library.core
  )
  .dependsOn(sparkCore)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkDs = project
  .in(file("modules/ds"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-ds",
    libraryDependencies ++= Seq(
      Library.ds, 
      Library.urlMng % Test
    )
  )
  .dependsOn(sparkCore, sparkRdd)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStreaming = project
  .in(file("modules/streaming"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-streaming",
    libraryDependencies ++= Seq(
      Library.streaming,
      Library.urlMng     % Test
    ),
    parallelExecution in Test := false
  )
  .dependsOn(sparkCore, sparkRdd)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val sparkStructuredStreaming = project
  .in(file("modules/structured-streaming"))
  .settings(headerSettings)
  .settings(Settings.common: _*)
  .settings(Settings.publish: _*)
  .settings(
    name := "chronicler-spark-structured-streaming",
    libraryDependencies ++= Seq(
      Library.ds,
      Library.urlMng % Test
    ) ++ Library.core
  )
  .dependsOn(sparkCore)
  .dependsOn(tests % "test->test")
  .enablePlugins(AutomateHeaderPlugin)

lazy val tests = project
  .in(file("modules/testing"))
  .settings(Settings.common: _*)
  .settings(
    name := "chronicler-spark-testing",
    libraryDependencies ++= Library.itTesting
  )