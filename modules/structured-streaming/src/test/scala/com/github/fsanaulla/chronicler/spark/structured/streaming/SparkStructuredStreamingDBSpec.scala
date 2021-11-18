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

package com.github.fsanaulla.chronicler.spark.structured.streaming

import com.github.fsanaulla.chronicler.core.alias.ErrorOr
import com.github.fsanaulla.chronicler.core.auth.{InfluxCredentials}
import com.github.fsanaulla.chronicler.core.model.{InfluxWriter}
import com.github.fsanaulla.chronicler.spark.testing.{DockerizedInfluxDB, SparkSessionBase}
import com.github.fsanaulla.chronicler.sync.io.{InfluxIO}
import com.github.fsanaulla.chronicler.sync.management.{InfluxMng}
import com.github.fsanaulla.chronicler.macros.auto._
import com.github.fsanaulla.chronicler.sync.shared.InfluxConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{TryValues, BeforeAndAfterAll, EitherValues}
import SparkStructuredStreamingDBSpec._

class SparkStructuredStreamingDBSpec
    extends SparkSessionBase
    with DockerizedInfluxDB
    with Eventually
    with TryValues
    with EitherValues
    with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    mng.close()
    io.close()

    super.afterAll()
  }

  val dbName = "db"

  implicit lazy val influxConf: InfluxConfig =
    InfluxConfig(host, port, Some(InfluxCredentials.Basic("admin", "password")))

  lazy val mng = InfluxMng(influxConf)
  lazy val io  = InfluxIO(influxConf)

  "Influx" - {
    "create database" in {
      mng.createDatabase(dbName).success.value.value mustEqual 200
    }

    "store data in database" - {

      "write" in {
        val schema = StructType(
          StructField("name", StringType) :: StructField("surname", StringType) :: Nil
        )

        spark.readStream
          .schema(schema)
          .csv(getClass.getResource("/structured").getPath)
          .writeStream
          .saveToInfluxDB(dbName)
          .start()
          .awaitTermination(1000 * 10)

        succeed
      }

      "check" in {
        eventually {
          io.database(dbName)
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

object SparkStructuredStreamingDBSpec {
  val meas = "meas"

  implicit val wr: InfluxWriter[Row] = new InfluxWriter[Row] {
    override def write(obj: Row): ErrorOr[String] = {
      val sb = StringBuilder.newBuilder

      sb.append(meas)
        .append(",")
        .append(s"name=${obj(0)}")
        .append(" ")
        .append("surname=")
        .append("\"")
        .append(obj(1))
        .append("\"")

      Right(sb.toString())
    }
  }
}
