package com.github.fsanaulla.chronicler.spark.testing

import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.time.{Millis, Seconds, Span}

/** It's base testing style that will be forced on the whole project
  */
trait BaseSpec extends AnyFreeSpec with Matchers with PatienceConfiguration {
  implicit val pc: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(60, Seconds)),
    interval = scaled(Span(1000, Millis))
  )
}
