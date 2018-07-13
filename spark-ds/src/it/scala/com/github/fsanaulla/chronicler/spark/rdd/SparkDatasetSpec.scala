package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials, InfluxFormatter}
import com.github.fsanaulla.chronicler.macros.Macros
import com.github.fsanaulla.chronicler.spark.ds._
import com.github.fsanaulla.chronicler.spark.tests.Models.Entity
import com.github.fsanaulla.chronicler.spark.tests.{DockerizedInfluxDB, Models}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.{FlatSpec, Matchers, TryValues}

class SparkDatasetSpec
  extends FlatSpec
    with Matchers
    with DockerizedInfluxDB
    with TryValues {

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")

  val spark: SparkSession = SparkSession
    .builder()
    .config(conf)
    .getOrCreate()

  val dbName = "db"
  val meas = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false)
  implicit val wr: InfluxFormatter[Entity] = Macros.format[Entity]

  import spark.implicits._

  "Influx" should "create database" in {
    val management = Influx.management(influxConf)
    management.createDatabase(dbName).success.value.isSuccess shouldEqual true
    management.close()
  }

  it should "save rdd to InfluxDB" in {
    Models.Entity.samples(20)
      .toDS()
      .saveToInflux(dbName, meas)
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
