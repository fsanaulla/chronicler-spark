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

package com.github.fsanaulla.chronicler.spark.streaming

import com.github.fsanaulla.chronicler.core.model.InfluxCredentials
import com.github.fsanaulla.chronicler.macros.auto._
import com.github.fsanaulla.chronicler.spark.testing.{DockerizedInfluxDB, BaseSpec, Entity}
import com.github.fsanaulla.chronicler.urlhttp.io.{InfluxIO, UrlIOClient}
import com.github.fsanaulla.chronicler.urlhttp.management.{InfluxMng, UrlManagementClient}
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{TryValues, BeforeAndAfterAll}

import scala.collection.mutable

class SparkStreamingSpec
    extends BaseSpec
    with DockerizedInfluxDB
    with Eventually
    with IntegrationPatience
    with TryValues
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    mng.close()
    io.close()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
    super.afterAll()
  }

  lazy val conf: SparkConf = new SparkConf()
    .setAppName("Rdd")
    .setMaster("local[*]")

  val sc: SparkContext = new SparkContext(conf)
  val ssc              = new StreamingContext(sc, Seconds(1))

  val dbName = "db"
  val meas   = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials("admin", "password")))

  lazy val mng: UrlManagementClient = InfluxMng(influxConf)
  lazy val io: UrlIOClient          = InfluxIO(influxConf)

  "Influx" - {
    "create database" in {
      mng.createDatabase(dbName).success.value.right.get shouldEqual 200
    }

    "store data in database" - {
      "write" in {
        val rdd = sc.parallelize(Entity.samples())

        // define stream
        ssc
          .queueStream(mutable.Queue(rdd))
          .saveToInfluxDBMeas(dbName, meas)

        ssc.start()

        // necessary stub
        Thread.sleep(22 * 1000)
      }

      "check" in {
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
  }
}
