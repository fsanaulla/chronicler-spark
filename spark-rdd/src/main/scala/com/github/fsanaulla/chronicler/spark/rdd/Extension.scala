package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.model.InfluxWriter
import org.apache.spark.rdd.RDD

object Extension {

  implicit final class RddOps[T](private val rdd: RDD[T]) extends AnyVal {
    def saveToInfluxc(implicit wr: InfluxWriter[T]): Unit = {
      rdd.foreachPartition { p =>
        ???
      }
    }
  }
}
