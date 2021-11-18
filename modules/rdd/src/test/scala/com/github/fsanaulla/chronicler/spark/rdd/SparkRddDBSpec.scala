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

import com.github.fsanaulla.chronicler.core.alias.ErrorOr
import com.github.fsanaulla.chronicler.core.auth.InfluxCredentials
import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.spark.testing.BaseSpec
import com.github.fsanaulla.chronicler.spark.testing.DockerizedInfluxDB
import com.github.fsanaulla.chronicler.spark.testing.Entity
import com.github.fsanaulla.chronicler.spark.testing.SparkContextBase
import com.github.fsanaulla.chronicler.sync.io.InfluxIO
import com.github.fsanaulla.chronicler.sync.management.InfluxMng
import com.github.fsanaulla.chronicler.sync.shared.InfluxConfig
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatest.BeforeAndAfterAll
import org.scalatest.EitherValues
import org.scalatest.TryValues
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.IntegrationPatience

import SparkRddDBSpec._

class SparkRddDBSpec
    extends SparkContextBase
    with Eventually
    with DockerizedInfluxDB
    with TryValues
    with EitherValues {

  override def afterAll(): Unit = {
    mng.close()
    io.close()

    super.afterAll()
  }

  val db   = "db"
  val meas = "meas"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials.Basic("admin", "password")))

  lazy val mng = InfluxMng(influxConf)
  lazy val io  = InfluxIO(influxConf)

  "Influx" - {
    "create database" in {
      mng.createDatabase(db).success.value.value mustEqual 200
    }

    "store data in database" - {

      "write" in {
        sc.parallelize(Entity.samples())
          .saveToInfluxDB(db)
          .mustEqual {}
      }

      "check" in {
        eventually {
          io.database(db)
            .readJson(s"SELECT * FROM $meas")
            .success
            .value
            .value
            .length mustEqual 20
        }
      }
    }
  }
}

object SparkRddDBSpec {
  implicit val wr: InfluxWriter[Entity] = new InfluxWriter[Entity] {
    override def write(obj: Entity): ErrorOr[String] =
      Right("meas,name=\"" + obj.name + "\" surname=\"" + obj.surname + "\"")

  }
}
