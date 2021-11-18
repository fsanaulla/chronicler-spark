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

package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.alias.ErrorOr
import com.github.fsanaulla.chronicler.core.either
import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.spark.core.{CallbackHandler, WriteConfig}
import com.github.fsanaulla.chronicler.sync.io.InfluxIO
import com.github.fsanaulla.chronicler.sync.shared.InfluxConfig
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

package object rdd {

  /** Extension that will provide static methods for saving RDDs to InfluxDB
    *
    * @param rdd
    *   - [[org.apache.spark.rdd.RDD]]
    * @tparam T
    *   - inner type
    */
  implicit final class RddOps[T](private val rdd: RDD[T]) extends AnyVal {

    private def handleResponse(
        handler: Option[CallbackHandler],
        response: Try[ErrorOr[Int]]
    ): Unit =
      if (handler.isDefined) {
        val Some(rh) = handler
        response match {
          case Success(Right(code)) => rh.onSuccess(code)
          // application level issues
          case Success(Left(ex)) => rh.onApplicationFailure(ex)
          // connection/network level issues
          case Failure(ex) => rh.onNetworkFailure(ex)
        }
      }

    /** Write [[org.apache.spark.rdd.RDD]] to InfluxDB, specifying database measurement
      *
      * @param dbName
      *   - database name
      * @param measName
      *   - measurement name
      * @param handler
      *   - defined callbacks for responses
      * @param dataInfo
      *   - data characteristics
      */
    def saveToInfluxDBMeas(
        dbName: String,
        measName: String,
        handler: Option[CallbackHandler] = None,
        dataInfo: WriteConfig = WriteConfig.default
    )(implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {
      rdd.foreachPartition { partition =>
        val client = InfluxIO(conf)
        val meas   = client.measurement[T](dbName, measName)

        partition.sliding(dataInfo.batchSize, dataInfo.batchSize).foreach { batch =>
          val response = meas.bulkWrite(
            batch,
            dataInfo.consistency,
            dataInfo.precision,
            dataInfo.retentionPolicy
          )

          // check if rh is defined
          handleResponse(handler, response)
        }

        client.close()
      }
    }

    /** Write [[org.apache.spark.rdd.RDD]] to InfluxDB, with measurements that generated dynamicly
      *
      * @param dbName
      *   - database name
      * @param handler
      *   - defined callbacks for responses
      * @param dataInfo
      *   - data characteristics
      */
    def saveToInfluxDB(
        dbName: String,
        handler: Option[CallbackHandler] = None,
        dataInfo: WriteConfig = WriteConfig.default
    )(implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {
      rdd.foreachPartition { partition =>
        val client = InfluxIO(conf)
        val db     = client.database(dbName)

        partition.sliding(dataInfo.batchSize, dataInfo.batchSize).foreach { batch =>
          val ethPoints = either.seq(batch.map(wr.write))
          val response = ethPoints
            .fold(Failure(_), Success(_))
            .flatMap { rows =>
              db.bulkWriteNative(
                rows,
                dataInfo.consistency,
                dataInfo.precision,
                dataInfo.retentionPolicy
              )

            }

          handleResponse(handler, response)
        }

        client.close()
      }
    }
  }
}
