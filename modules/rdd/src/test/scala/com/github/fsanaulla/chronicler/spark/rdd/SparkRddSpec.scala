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

package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.InfluxCredentials
import com.github.fsanaulla.chronicler.macros.auto._
import com.github.fsanaulla.chronicler.spark.testing.{DockerizedInfluxDB, BaseSpec, Entity}
import com.github.fsanaulla.chronicler.urlhttp.io.{InfluxIO, UrlIOClient}
import com.github.fsanaulla.chronicler.urlhttp.management.{InfluxMng, UrlManagementClient}
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{TryValues, BeforeAndAfterAll, EitherValues}

class SparkRddSpec
    extends BaseSpec
    with Eventually
    with IntegrationPatience
    with DockerizedInfluxDB
    with TryValues
    with EitherValues
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    mng.close()
    io.close()
    sc.stop()
    super.afterAll()
  }

  val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)

  val db   = "db"
  val meas = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")))

  lazy val mng: UrlManagementClient = InfluxMng(influxConf)
  lazy val io: UrlIOClient          = InfluxIO(influxConf)

  "Influx" - {
    "create database" in {
      mng.createDatabase(db).success.value.right.get shouldEqual 200
    }

    "store data in database" - {
      "write" in {
        sc.parallelize(Entity.samples())
          .saveToInfluxDBMeas(db, meas)
          .shouldEqual {}
      }

      "check" in {
        eventually {
          io.database(db)
            .readJson(s"SELECT * FROM $meas")
            .success
            .value
            .value
            .length shouldEqual 20
        }
      }
    }
  }
}
