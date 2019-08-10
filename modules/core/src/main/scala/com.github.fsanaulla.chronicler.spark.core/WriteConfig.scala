/*
 * Copyright 2018-2019 Faiaz Sanaulla
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fsanaulla.chronicler.spark.core

import com.github.fsanaulla.chronicler.core.enums.{Consistencies, Consistency, Precision, Precisions}

/**
  * Configuration for write operation
  *
  * @param batchSize       - count of  points in one batch
  * @param consistency     - data consistency
  * @param precision       - data precision
  * @param retentionPolicy - data retention policy
  */
final case class WriteConfig(batchSize: Int,
                             consistency: Consistency,
                             precision: Precision,
                             retentionPolicy: Option[String])

object WriteConfig {
  // default entry
  val default = WriteConfig(2500, Consistencies.None, Precisions.None, None)
}
