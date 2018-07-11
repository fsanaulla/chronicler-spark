package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxFormatter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

package object rdd {

  /**
    * Extension that will provide static methods for saving RDDs to InfluxDB
    *
    * @param rdd - Spark RDD
    * @tparam T  - RDD inner type
    */
  implicit final class RddOps[T](private val rdd: RDD[T]) extends AnyVal {

    /**
      * Write rdd to influxdb
      *
      * @param dbName   - influxdb name
      * @param measName - measurement name
      * @param fmt      - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String)(implicit fmt: InfluxFormatter[T], conf: InfluxConfig, tt: ClassTag[T]): Unit = {


      rdd.foreachPartition { partition =>

        val influx = Influx.io(conf)
        val meas = influx.measurement[T](dbName, measName)

        partition.foreach { e =>
          meas.write(e)
        }

        influx.close()
      }

    }
  }
}
