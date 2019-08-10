# chronicler-spark
[![Build Status](https://travis-ci.org/fsanaulla/chronicler-spark.svg?branch=master)](https://travis-ci.org/fsanaulla/chronicler-spark)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/chronicler-spark-rdd_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/chronicler-spark-rdd_2.11)
[![Join the chat at https://gitter.im/chronicler/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/chronicler-scala/Lobby/?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6ae4488ff28d4442895c1be378281a03)](https://www.codacy.com/app/fsanaulla/chronicler-spark?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fsanaulla/chronicler-spark&amp;utm_campaign=Badge_Grade)

Open-source [InfluxDB](https://www.influxdata.com/) connector for [Apache Spark](https://spark.apache.org/index.html) on top of [Chronicler](https://github.com/fsanaulla/chronicler).

## Get Started

At the beginning add required module to your `build.sbt`:

```
// For RDD
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-rdd" % <version>

// For Dataset
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-ds" % <version>

// For Structured Streaming
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-structured-streaming" % <version>

// For DStream
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-streaming" % <version>
```

## Usage

Default configuration: 
```
final case class InfluxConfig(
    host: String,
    port: Int = 8086,
    credentials: Option[InfluxCredentials] = None,
    compress: Boolean = false,
    ssl: Boolean = false)
```
It's recommended to enable data compression to decrease network traffic.

For `RDD[T]`:

```
import com.github.fsanaulla.chronicler.spark.rdd._

val rdd: RDD[T] = _
rdd.saveToInfluxDB("dbName", "measurementName")
```
For `Dataset[T]`:
```
import com.github.fsanaulla.chronicler.spark.ds._

val ds: Dataset[T] = _
ds.saveToInfluxDB("dbName", "measurementName")
```
For `DataStreamWriter[T]`
```
import com.github.fsanaulla.chronicler.spark.structured.streaming._

val structStream: DataStreamWriter[T] = _
val saved = structStream.saveToInfluxDB("dbName", "measurementName")
..
saved.start().awaitTermination()

```

For `DStream[T]`:
```
import com.github.fsanaulla.chronicler.spark.streaming._

val stream: DStream[T] = _
stream.saveToInfluxDB("dbName", "measurementName")
```
