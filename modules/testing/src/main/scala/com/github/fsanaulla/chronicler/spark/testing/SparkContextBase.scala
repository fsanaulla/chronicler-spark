package com.github.fsanaulla.chronicler.spark.testing

import org.scalatest.BeforeAndAfterAll
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.Suite

trait SparkContextBase extends BaseSpec with SparkConfBase { suite: Suite =>

  protected lazy val sc: SparkContext = new SparkContext(conf)

  override def beforeAll(): Unit = {
    sc
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    sc.stop()
    super.afterAll()
  }
}
