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

package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.spark.core.{CallbackHandler, WriteConfig}
import com.github.fsanaulla.chronicler.spark.rdd._
import com.github.fsanaulla.chronicler.urlhttp.shared.InfluxConfig
import org.apache.spark.sql.Dataset

import scala.reflect.ClassTag

package object ds {

  /**
    * Extension that will provide static methods for saving [[Dataset]] to InfluxDB
    *
    * @param ds - [[Dataset]]
    * @tparam T - inner type
    */
  implicit final class DatasetOps[T](private val ds: Dataset[T]) extends AnyVal {

    /**
      * Write Spark [[Dataset]] to InfluxDB
      *
      * @param dbName          - database name
      * @param measName        - measurement name
      */
    def saveToInfluxDB(dbName: String,
                       measName: String,
                       ch: Option[CallbackHandler] = None,
                       dataInfo: WriteConfig = WriteConfig.default)
                      (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {
      // it throw compiler error when using it, on ds
      ds.rdd.saveToInfluxDB(dbName, measName, ch, dataInfo)
    }

    /**
      * Write Spark [[Dataset]] to InfluxDB
      *
      * @param dbName          - database name
      */
    def saveToInfluxDB(dbName: String,
                       ch: Option[CallbackHandler] = None,
                       dataInfo: WriteConfig = WriteConfig.default)
                      (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {
      // it throw compiler error when using it, on ds
      ds.rdd.saveToInfluxDB(dbName, ch, dataInfo)
    }
  }
}
