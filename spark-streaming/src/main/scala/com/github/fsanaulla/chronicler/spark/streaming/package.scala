package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.streaming.dstream.DStream

package object streaming {

  /**
    * Extension that will provide static methods for saving DStream to InfluxDB
    *
    * @param stream - Spark DStream
    * @tparam T  - DStream inner type
    */
  implicit final class DStreamOps[T](private val stream: DStream[T]) extends AnyVal {

    /**
      * Write stream to influxdb
      *
      * @param dbName   - InfluxDB name
      * @param measName - measurement name
      * @param wr       - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String)(implicit wr: InfluxWriter[T], conf: InfluxConfig): Unit = {

      val influx = Influx.io(conf)
      val meas = influx.measurement[T](dbName, measName)
      stream.foreachRDD { rdd =>
        rdd.foreachPartition { part =>
          part.foreach { t =>
            meas.write(t)
          }
        }
      }
    }
  }
}
