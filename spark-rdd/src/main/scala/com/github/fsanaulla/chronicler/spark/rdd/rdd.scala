package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import com.github.fsanaulla.chronicler.urlhttp.Influx
import org.apache.spark.rdd.RDD

object rdd {

  implicit final class RddOps[T](private val rdd: RDD[T]) extends AnyVal {

    /**
      * Write rdd to influxdb
      * @param dbName   - influxdb name
      * @param measName - measurement name
      * @param wr       - implicit influx writer
      */
    def saveToInflux(dbName: String,
                     measName: String)(implicit wr: InfluxWriter[T]): Unit = {

      val influx = Influx.connect() // todo: add influx config
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
