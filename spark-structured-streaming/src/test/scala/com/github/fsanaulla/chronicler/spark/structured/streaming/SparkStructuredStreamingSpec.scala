package com.github.fsanaulla.chronicler.spark.structured.streaming

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials, InfluxWriter}
import com.github.fsanaulla.chronicler.spark.tests.DockerizedInfluxDB
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.{FlatSpec, Matchers, TryValues}


class SparkStructuredStreamingSpec
  extends FlatSpec
  with Matchers
  with DockerizedInfluxDB
  with TryValues {

    val conf: SparkConf = new SparkConf()
      .setAppName("ss")
      .setMaster("local[*]")

    val spark: SparkSession = SparkSession
      .builder()
      .config(conf)
      .getOrCreate()

    val dbName = "db"
    val meas = "meas"

    implicit lazy val influxConf: InfluxConfig =
      InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false)
    implicit val wr: InfluxWriter[Row] = new InfluxWriter[Row] {
      override def write(obj: Row): String = {
        val sb = StringBuilder.newBuilder

        sb.append(s"name=${obj(0)}")
          .append(" ")
          .append("surname=")
          .append("\"")
          .append(obj(1))
          .append("\"")

        sb.toString()
      }
    }

    "Influx" should "create database" in {
      val management = Influx.management(influxConf)
      management.createDatabase(dbName).success.value.isSuccess shouldEqual true
      management.close()
    }

    it should "save structured stream to InfluxDB" in {
      val schema = StructType(
        StructField("name", StringType) :: StructField("surname", StringType) :: Nil
      )

      spark
        .readStream
        .schema(schema)
        .csv(getClass.getResource("/structured/").getPath)
        .writeStream
        .saveToInflux(dbName, meas)
        .start()
        .awaitTermination(1000 * 10)

      succeed
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
