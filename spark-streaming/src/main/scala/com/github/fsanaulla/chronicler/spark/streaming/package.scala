package com.github.fsanaulla.chronicler.spark

import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.streaming.dstream.DStream

package object streaming {

  implicit final class DStreamOps[T](private val dstream: DStream[T]) extends AnyVal {
    def savaToInflux(dbName: String,
                     measName: String)(implicit wr: InfluxWriter[T], conf: InfluxConfig): Unit = {

      val influx = Influx.io(conf)
      val meas = influx.measurement[T](dbName, measName)
      dstream.foreachRDD { rdd =>
        rdd.foreachPartition { part =>
          part.foreach { t =>
            meas.write(t)
          }
        }
      }
    }
  }
}
