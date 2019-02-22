/*
 * Copyright 2018 Faiaz Sanaulla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fsanaulla.chronicler.spark.structured.streaming

import com.github.fsanaulla.chronicler.core.model.{InfluxCredentials, InfluxWriter}
import com.github.fsanaulla.chronicler.spark.tests.DockerizedInfluxDB
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO
import com.github.fsanaulla.chronicler.urlhttp.management.InfluxMng
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{FlatSpec, Matchers, TryValues}

class SparkStructuredStreamingSpec
  extends FlatSpec
    with Matchers
    with DockerizedInfluxDB
    with Eventually
    with IntegrationPatience
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
      InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false, None)
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
      val management = InfluxMng(host, port, Some(InfluxCredentials("admin", "password")), None)
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
        .csv(getClass.getResource("/structured").getPath)
        .writeStream
        .saveToInfluxDB(dbName, meas)
        .start()
        .awaitTermination(1000 * 10)

      succeed
    }

    it should "retrieve saved items" in {
      val influx = InfluxIO(influxConf)
      val db = influx.database(dbName)

      eventually {
        db.readJs(s"SELECT * FROM $meas")
          .success
          .value
          .queryResult
          .length shouldEqual 20
      }

      influx.close()
    }
}
