package com.github.fsanaulla.chronicler.spark.structured

import com.github.fsanaulla.chronicler.core.enums.{Consistencies, Consistency, Precision, Precisions}
import com.github.fsanaulla.chronicler.core.model.{InfluxConfig, InfluxWriter}
import org.apache.spark.sql.streaming.DataStreamWriter

import scala.reflect.ClassTag

package object streaming {

  implicit final class DataStreamWriterOps[T](private val dsw: DataStreamWriter[T]) extends AnyVal {

    /**
      * Write Spark structured streaming to InfluxDB
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
                    (implicit wr: InfluxWriter[T], conf: InfluxConfig, tt: ClassTag[T]): DataStreamWriter[T] = {
      dsw.foreach(new InfluxForeachWriter[T](dbName, measName, consistency, precision, retentionPolicy))
    }
  }
}
