package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.sql.Dataset

package object ds {

  /**
    * Extension that will provide static methods for saving Dataset[T] to InfluxDB
    *
    * @param ds - Spark Dataset of type T
    * @tparam T  - Dataset inner type
    */
  implicit final class DataFrameOps[T](private val ds: Dataset[T]) extends AnyVal {

    /**
      * Write Dataset[T] to InfluxDB
      *
      * @param dbName   - influxdb name
      * @param measName - measurement name
      * @param wr       - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String)(implicit wr: InfluxWriter[T], conf: InfluxConfig): Unit = {

      val influx = Influx.io(conf)
      val meas = influx.measurement[T](dbName, measName)

      ds.foreachPartition { part =>
        part.foreach { t =>
          meas.write(t)
        }
      }

      influx.close()
    }
  }
}
