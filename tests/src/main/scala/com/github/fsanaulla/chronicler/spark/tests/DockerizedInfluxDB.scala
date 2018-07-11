package com.github.fsanaulla.chronicler.spark.tests

import org.scalatest.{BeforeAndAfterAll, Suite}
import org.testcontainers.containers.InfluxDBContainer

trait DockerizedInfluxDB extends BeforeAndAfterAll { self: Suite =>

  private val influx = new InfluxDBContainer()

  /** host address */
  def host: String = influx.getContainerIpAddress

  /** mapped port */
  def port: Int = influx.getLivenessCheckPortNumbers.toArray.head.asInstanceOf[Int]

  override def beforeAll(): Unit = {
    super.beforeAll()

    influx.start()
  }

  override def afterAll(): Unit = {
    super.afterAll()

    influx.stop()
  }
}
