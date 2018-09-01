# chronicler-spark
[![Build Status](https://travis-ci.org/fsanaulla/chronicler-spark.svg?branch=master)](https://travis-ci.org/fsanaulla/chronicler-spark)
[![Join the chat at https://gitter.im/chronicler/Lobby](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/chronicler-scala/Lobby/?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Open-source [InfluxDB](https://www.influxdata.com/) connector for [Apache Spark](https://spark.apache.org/index.html) on top of [Chronicler](https://github.com/fsanaulla/chronicler).

## Modules
| Module | Description | Version |
| ------------- | ------------- | ---------- |
| `chronicler-spark-rdd` | Spark RDD's extensions | [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler-spark/chronicler-spark-rdd/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-rdd_2.11) |
| `chronicler-spark-dataset` | Spark Dataset's extension| [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler-spark/chronicler-spark-ds/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-ds_2.11) |
| `chronicler-spark-structured-streaming` | Spark structured streaming extension| [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler-spark/chronicler-spark-structured-streaming/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-structured-streaming_2.11) |
| `chronicler-spark-streaming` | Spark DStream's extension | [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler-spark/chronicler-spark-streaming/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-streaming_2.11) |

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

For `RDD[T]`:

```
import com.github.fsanaulla.chronicler.spark.rdd._

val rdd: RDD[T] = _
rdd.saveToInflux("dbName", "measurementName")
```
For `Dataset[T]`:
```
import com.github.fsanaulla.chronicler.spark.ds._

val ds: Dataset[T] = _
ds.saveToInflux("dbName", "measurementName")
```
For `DataStreamWriter[T]`
```
import com.github.fsanaulla.chronicler.spark.structured.streaming._

val structStream: DataStreamWriter[T] = _
val saved = structStream.saveToInflux("dbName", "measurementName")
..
saved.start().awaitTermination()

```

For `DStream[T]`:
```
import com.github.fsanaulla.chronicler.spark.streaming._

val stream: DStream[T] = _
stream.saveToInflux("dbName", "measurementName")
```
## Information
It's compiled over officially supported scala versions by Spark, at the moment it's 2.11.
