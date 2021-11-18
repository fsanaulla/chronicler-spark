package com.github.fsanaulla.chronicler.spark.testing

import org.apache.spark.SparkConf
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SparkConfBase extends BeforeAndAfterAll { self: Suite =>
  protected val conf = new SparkConf()
    .setAppName("appName")
    .setMaster("local[*]")
    .set("spark.driver.bindAddress", "127.0.0.1")
}
