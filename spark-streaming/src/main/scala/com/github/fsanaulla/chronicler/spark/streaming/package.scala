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

package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.enums.{Consistency, Precision}
import com.github.fsanaulla.chronicler.core.model.{InfluxWriter, WriteResult}
import com.github.fsanaulla.chronicler.urlhttp.io.InfluxIO
import com.github.fsanaulla.chronicler.urlhttp.io.models.InfluxConfig
import org.apache.spark.streaming.dstream.DStream
import resource._

import scala.reflect.ClassTag
import scala.util.{Failure, Success}

package object streaming {

  /**
    * Extension that will provide static methods for saving DStream to InfluxDB
    *
    * @param stream - [[DStream]]
    * @tparam T     - inner type
    */
  implicit final class DStreamOps[T](private val stream: DStream[T]) extends AnyVal {

    /**
      * Write [[DStream]] to InfluxDB
      *
      * @param dbName          - database name
      * @param measName        - measurement name
      * @param onFailure       - function to handle failed cases
      * @param onSuccess       - function to handle success case
      * @param consistency     - consistence level
      * @param precision       - time precision
      * @param retentionPolicy - retention policy type
      * @param wr              - implicit [[InfluxWriter]]
      */
    def saveToInfluxDB(dbName: String,
                       measName: String,
                       onFailure: Throwable => Unit = _ => (),
                       onSuccess: WriteResult => Unit = _ => (),
                       consistency: Option[Consistency] = None,
                       precision: Option[Precision] = None,
                       retentionPolicy: Option[String] = None)
                      (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {
      stream.foreachRDD { rdd =>
        rdd.foreachPartition { partition =>
          managed(InfluxIO(conf)) map { cl =>
            val meas = cl.measurement[T](dbName, measName)
            meas.bulkWrite(partition.toSeq, consistency, precision, retentionPolicy) match {
              case Success(value) => onSuccess(value)
              case Failure(ex) => onFailure(ex)
            }
          }
        }
      }
    }
  }
}
