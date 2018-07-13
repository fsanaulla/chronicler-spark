package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials, InfluxFormatter}
import com.github.fsanaulla.chronicler.macros.Macros
import com.github.fsanaulla.chronicler.spark.streaming._
import com.github.fsanaulla.chronicler.spark.tests.Models.Entity
import com.github.fsanaulla.chronicler.spark.tests.{DockerizedInfluxDB, Models}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FlatSpec, Matchers, TryValues}

import scala.collection.mutable

class SparkStreamingSpec
  extends FlatSpec
    with Matchers
    with DockerizedInfluxDB
    with TryValues {

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)
  val ssc  = new StreamingContext(sc, Seconds(1))

  val dbName = "db"
  val meas = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false)
  implicit val wr: InfluxFormatter[Entity] = Macros.format[Entity]

  "Influx" should "create database" in {
    val management = Influx.management(influxConf)
    management.createDatabase(dbName).success.value.isSuccess shouldEqual true
    management.close()
  }

  it should "save rdd to InfluxDB" in {
    val rdd = sc.parallelize(Models.Entity.samples(20))

    // define stream
    ssc
      .queueStream(mutable.Queue(rdd))
      .saveToInflux(dbName, meas)

    ssc.start()

    // necessary stub
    Thread.sleep(22 * 1000)

    ssc.stop()
  }

  it should "retrieve saved items" in {
    val influx = Influx.io(influxConf)
    val db = influx.database(dbName)

    db.readJs(s"SELECT * FROM $meas")
      .success
      .value
      .queryResult
      .length shouldEqual 20

    influx.close()
  }
}
