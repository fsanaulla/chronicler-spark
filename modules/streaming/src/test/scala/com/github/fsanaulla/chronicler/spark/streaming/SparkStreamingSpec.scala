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

import com.github.fsanaulla.chronicler.core.auth.InfluxCredentials
import com.github.fsanaulla.chronicler.macros.auto._
import com.github.fsanaulla.chronicler.spark.testing.{DockerizedInfluxDB, SparkContextBase, Entity}
import com.github.fsanaulla.chronicler.sync.io.{InfluxIO}
import com.github.fsanaulla.chronicler.sync.management.{InfluxMng}
import com.github.fsanaulla.chronicler.sync.shared.InfluxConfig
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{TryValues, BeforeAndAfterAll, EitherValues}

import scala.collection.mutable

class SparkStreamingSpec
    extends SparkContextBase
    with DockerizedInfluxDB
    with Eventually
    with TryValues
    with EitherValues {

  override def afterAll(): Unit = {
    mng.close()
    io.close()

    ssc.stop(stopSparkContext = false, stopGracefully = true)
    super.afterAll()
  }

  val ssc    = new StreamingContext(sc, Seconds(1))
  val dbName = "db"
  val meas   = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials.Basic("admin", "password")))

  lazy val mng = InfluxMng(influxConf)
  lazy val io          = InfluxIO(influxConf)

  "Influx" - {
    "create database" in {
      mng.createDatabase(dbName).success.value.value mustEqual 200
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
            .length mustEqual 20
        }
      }
    }
  }
}
