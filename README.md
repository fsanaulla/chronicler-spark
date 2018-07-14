# Chronicler-spark
Open-source [InfluxDB](https://www.influxdata.com/) connector for [Apache Spark](https://spark.apache.org/index.html) on top of [Chronicler](https://github.com/fsanaulla/chronicler).

[![Build Status](https://travis-ci.org/fsanaulla/chronicler-spark.svg?branch=master)](https://travis-ci.org/fsanaulla/chronicler-spark)

## Modules
| Module | Description | Version |
| ------------- | ------------- | ---------- |
| `chronicler-spark-rdd` | Spark RDD's extensions | [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler-spark/chronicler-spark-rdd/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-rdd_2.11) |
| `chronicler-spark-dataset` | Spark Dataset's extension| [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler/chronicler-spark-ds/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-ds_2.11) |
| `chronicler-spark-streaming` | Spark DStream's extension | [![Latest version](https://index.scala-lang.org/fsanaulla/chronicler/chronicler-spark-streaming/latest.svg?color=yellow)](https://index.scala-lang.org/com.github.fsanaulla/chronicler-spark/chronicler-spark-streaming_2.11) |

## Get Started

At the beginning add required module to your `build.sbt`:

```
// For RDD
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-rdd" % <version>

// For Dataset
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-ds" % <version>

// For DStream
libraryDependencies += "com.github.fsanaulla" %% "chronicler-spark-streaming" % <version>
```
## Information
It's compiled over officially supported scala versions by Spark, at the moment it's 2.11.