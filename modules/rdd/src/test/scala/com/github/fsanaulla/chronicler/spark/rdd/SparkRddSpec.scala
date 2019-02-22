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

package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.{InfluxCredentials, InfluxFormatter}
import com.github.fsanaulla.chronicler.macros.Influx
import com.github.fsanaulla.chronicler.spark.tests.Models.Entity
import com.github.fsanaulla.chronicler.spark.tests.{DockerizedInfluxDB, Models}
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO
import com.github.fsanaulla.chronicler.urlhttp.management.InfluxMng
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{FlatSpec, Matchers, TryValues}
import resource._

class SparkRddSpec
  extends FlatSpec
    with Matchers
    with Eventually
    with IntegrationPatience
    with DockerizedInfluxDB
    with TryValues {

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")
  val sc: SparkContext = new SparkContext(conf)
  val dbName = "db"
  val meas = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")), gzipped = false, None)
  implicit val wr: InfluxFormatter[Entity] = Influx.formatter[Entity]

  "Influx" should "create database" in {
    managed(InfluxMng(host, port, Some(InfluxCredentials("admin", "password")), None)) map { cl =>
      cl.createDatabase(dbName).success.value.isSuccess shouldEqual true
    }
  }

  it should "save rdd to InfluxDB using writer" in {
    sc
      .parallelize(Models.Entity.samples())
      .saveToInfluxDB(dbName, meas)
      .shouldEqual {}
  }

  it should "save rdd to InfluxDB using custom serialization" in {
    sc
      .parallelize(Models.Entity.samples())
      .saveToInfluxDBCustom(dbName, e => s"meas,name=${e.name} surname=${e.surname}")
      .shouldEqual {}
  }

  it should "retrieve saved items" in {
    managed(InfluxIO(influxConf)) map { cl =>
      eventually {
        cl.database(dbName).readJs("SELECT * FROM meas").success.value.queryResult.length shouldEqual 40
      }
    }
  }
}
