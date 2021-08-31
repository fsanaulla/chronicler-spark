/*
 * Copyright 2018-2021 Faiaz Sanaulla
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
import com.github.fsanaulla.chronicler.urlhttp.io.{InfluxIO, UrlIOClient}
import com.github.fsanaulla.chronicler.urlhttp.management.{InfluxMng, UrlManagementClient}
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{FlatSpec, Matchers, TryValues}

class SparkDatasetMeasSpec
    extends FlatSpec
    with Matchers
    with Eventually
    with IntegrationPatience
    with DockerizedInfluxDB
    with TryValues {

  override def afterAll(): Unit = {
    mng.close()
    io.close()
    spark.close()
    super.afterAll()
  }

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")

  val spark: SparkSession = SparkSession
    .builder()
    .config(conf)
    .getOrCreate()

  val dbName = "db"
  val meas   = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(s"http://$host", port, Some(InfluxCredentials("admin", "password")))

  lazy val mng: UrlManagementClient = InfluxMng(influxConf)
  lazy val io: UrlIOClient          = InfluxIO(influxConf)

  import spark.implicits._

  "Influx" should "create database" in {
    mng.createDatabase(dbName).success.value.right.get shouldEqual 200
  }

  it should "save rdd to InfluxDB" in {
    Entity
      .samples()
      .toDS()
      .saveToInfluxDBMeas(dbName, meas)
      .shouldEqual {}
  }

  it should "retrieve saved items" in {
    eventually {
      io.database(dbName)
        .readJson("SELECT * FROM meas")
        .success
        .value
        .right
        .get
        .length shouldEqual 20
    }
  }
}
