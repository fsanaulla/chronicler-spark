/*
 * Copyright 2018-2019 Faiaz Sanaulla
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

package com.github.fsanaulla.chronicler.spark.ds

import com.github.fsanaulla.chronicler.core.model.InfluxCredentials
import com.github.fsanaulla.chronicler.macros.auto._
import com.github.fsanaulla.chronicler.spark.tests.{DockerizedInfluxDB, Entity}
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO
import com.github.fsanaulla.chronicler.urlhttp.management.InfluxMng
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{FlatSpec, Matchers, TryValues}

class SparkDatasetSpec
  extends FlatSpec
    with Matchers
    with Eventually
    with IntegrationPatience
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
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false, None)

  import spark.implicits._

  "Influx" should "create database" in {
    val mng = InfluxMng(host, port, Some(InfluxCredentials("admin", "password")), None)

    mng.createDatabase(dbName).success.value.right.get shouldEqual 200

    mng.close() shouldEqual {}
  }

  it should "save rdd to InfluxDB" in {
    Entity.samples()
      .toDS()
      .saveToInfluxDB(dbName, meas)
      .shouldEqual {}
  }

  it should "retrieve saved items" in {
    val cl = InfluxIO(influxConf)

    eventually {
      cl.database(dbName).readJson("SELECT * FROM meas").success.value.right.get.length shouldEqual 20
    }

    cl.close() shouldEqual {}
  }
}
