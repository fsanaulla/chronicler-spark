package com.github.fsanaulla.chronicler.spark.testing

import org.apache.spark.sql.SparkSession
import org.scalatest.Suite

trait SparkSessionBase extends BaseSpec with SparkConfBase { self: Suite =>

  lazy val spark: SparkSession = SparkSession
    .builder()
    .config(conf)
    .getOrCreate()

  override def beforeAll(): Unit = {
    spark

    super.beforeAll()
  }

  override def afterAll(): Unit = {
    spark.stop()

    super.afterAll()
  }
}
