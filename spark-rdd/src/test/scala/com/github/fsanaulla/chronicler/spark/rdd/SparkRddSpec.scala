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

package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxCredentials, InfluxFormatter}
import com.github.fsanaulla.chronicler.macros.Macros
import com.github.fsanaulla.chronicler.spark.tests.Models.Entity
import com.github.fsanaulla.chronicler.spark.tests.{DockerizedInfluxDB, Models}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{FlatSpec, Matchers, TryValues}

class SparkRddSpec
  extends FlatSpec
    with Matchers
    with DockerizedInfluxDB
    with TryValues {

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")
  val sc: SparkContext = new SparkContext(conf)
  val influxDbName = "db"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false)
  implicit val wr: InfluxFormatter[Entity] = Macros.format[Entity]

  "Influx" should "create database" in {
    val management = Influx.management(influxConf)
    management.createDatabase(influxDbName).success.value.isSuccess shouldEqual true
    management.close()
  }

  it should "save rdd to InfluxDB" in {
    sc
      .parallelize(Models.Entity.samples())
      .saveToInflux("db", "meas")
      .shouldEqual {}
  }

  it should "retrieve saved items" in {
    val influx = Influx.io(influxConf)
    val db = influx.database(influxDbName)

    db.readJs("SELECT * FROM meas")
      .success
      .value
      .queryResult
      .length shouldEqual 20

    influx.close()
  }
}
