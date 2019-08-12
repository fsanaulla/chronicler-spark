package com.github.fsanaulla.chronicler.spark.rdd

import com.github.fsanaulla.chronicler.core.alias.JPoint
import com.github.fsanaulla.chronicler.core.enums.{Epoch, Epochs}
import com.github.fsanaulla.chronicler.spark.rdd.InfluxRDD.InfluxPartition
import com.github.fsanaulla.chronicler.urlhttp.io.UrlIOClient
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, SparkContext, TaskContext}

class InfluxRDD[T](sc: SparkContext,
                   sql: String,
                   dbName: String,
                   getClient: () => UrlIOClient,
                   numPartitions: Int,
                   lowerBound: Long,
                   upperBound: Long,
                   batchMapping: Array[JPoint] => T) extends RDD[T](sc, Nil) {
  override def compute(split: Partition, context: TaskContext): Iterator[T] = {
    val cl = getClient()
    val db = cl.database(dbName)
    val part = split.asInstanceOf[InfluxPartition]

    context.addTaskCompletionListener(_ => cl.close())

    db
      .readChunkedJson(sql).map {
        case Right(value) => value
        case Left(err)    => throw err
      }
      .map(batchMapping)
  }

  override def getPartitions: Array[Partition] = {
    // bounds are inclusive, hence the + 1 here and - 1 on end
    val length = BigInt(1) + upperBound - lowerBound
    (0 until numPartitions).map { i =>
      val start = lowerBound + ((i * length) / numPartitions)
      val end = lowerBound + (((i + 1) * length) / numPartitions) - 1
      new InfluxPartition(i, start.toLong, end.toLong)
    }.toArray
  }
}

object InfluxRDD {
  final class InfluxPartition(idx: Int, val from: Long, val to: Long) extends Partition {
    override def index: Int = idx
  }

  final case class ReadConfig(numOfPartition: Int,
                              chunkSize: Int = 10000,
                              epoch: Epoch = Epochs.None)
}
