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

import com.github.fsanaulla.chronicler.core.enums.{Consistencies, Consistency, Precision, Precisions}
import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.sql.Dataset

import scala.reflect.ClassTag

package object ds {

  /**
    * Extension that will provide static methods for saving Dataset[T] to InfluxDB
    *
    * @param ds - Spark Dataset of type T
    * @tparam T  - Dataset inner type
    */
  implicit final class DatasetOps[T](private val ds: Dataset[T]) extends AnyVal {

    /**
      * Write Spark Dataset[T] to InfluxDB
      *
      * @param dbName   - influxdb name
      * @param measName - measurement name
      * @param wr       - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String,
                     consistency: Consistency = Consistencies.ONE,
                     precision: Precision = Precisions.NANOSECONDS,
                     retentionPolicy: Option[String] = None)
                    (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {


      ds.foreachPartition { partition =>

        val influx = Influx.io(conf)
        val meas = influx.measurement[T](dbName, measName)

        partition.foreach { e =>
          meas.write(e, consistency, precision, retentionPolicy)
        }

        influx.close()
      }
    }
  }
}
