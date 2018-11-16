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

package com.github.fsanaulla.chronicler.spark.structured

import com.github.fsanaulla.chronicler.core.enums.{Consistency, Precision}
import com.github.fsanaulla.chronicler.core.model.{InfluxWriter, WriteResult}
import com.github.fsanaulla.chronicler.urlhttp.io.models.InfluxConfig
import org.apache.spark.sql.streaming.DataStreamWriter

import scala.reflect.ClassTag

package object streaming {

  implicit final class DataStreamWriterOps[T](private val dsw: DataStreamWriter[T]) extends AnyVal {

    /**
      * Write Spark structured streaming to InfluxDB
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
                      (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): DataStreamWriter[T] = {
      dsw.foreach(
        new InfluxForeachWriter[T](dbName, measName, onFailure, onSuccess, consistency, precision, retentionPolicy))
    }
  }
}
