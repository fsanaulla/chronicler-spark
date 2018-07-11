package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.rdd.RDD

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
      * @param wr       - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String)(implicit wr: InfluxWriter[T], conf: InfluxConfig): Unit = {

      val influx = Influx.io(conf)
      val meas = influx.measurement[T](dbName, measName)

      rdd.foreachPartition { p =>
        p.foreach { e =>
          meas.write(e)
        }
      }

      influx.close()
    }
  }
}
