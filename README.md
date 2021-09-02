# chronicler-spark

[![Scala CI](https://github.com/fsanaulla/spark-http-rdd/actions/workflows/scala.yml/badge.svg)](https://github.com/fsanaulla/chronicler-spark/actions/workflows/scala.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/chronicler-spark-core_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.fsanaulla/chronicler-spark-core_2.12)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

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
rdd.saveToInfluxDBMeas("dbName", "measurementName")

// to save with dynamicly generated measurement
rdd.saveToInfluxDB("dbName")
```
For `Dataset[T]`:
```
import com.github.fsanaulla.chronicler.spark.ds._

val ds: Dataset[T] = _
ds.saveToInfluxDBMeas("dbName", "measurementName")

// to save with dynamicly generated measurement
ds.saveToInfluxDB("dbName")

```
For `DataStreamWriter[T]`
```

import com.github.fsanaulla.chronicler.spark.structured.streaming._

val structStream: DataStreamWriter[T] = _
val saved = structStream.saveToInfluxDBMeas("dbName", "measurementName")

// to save with dynamicly generated measurement
val saved = structStream.saveToInfluxDB("dbName")
..
saved.start().awaitTermination()

```

For `DStream[T]`:
```
import com.github.fsanaulla.chronicler.spark.streaming._

val stream: DStream[T] = _
stream.saveToInfluxDBMeas("dbName", "measurementName")
stream,saveToInfluxDB("dbName")
```
