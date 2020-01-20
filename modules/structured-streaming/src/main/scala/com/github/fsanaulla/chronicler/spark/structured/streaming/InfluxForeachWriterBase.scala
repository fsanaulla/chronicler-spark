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

package com.github.fsanaulla.chronicler.spark.structured.streaming

import com.github.fsanaulla.chronicler.core.alias.ErrorOr
import com.github.fsanaulla.chronicler.spark.core.CallbackHandler

import scala.util.{Failure, Success, Try}

trait InfluxForeachWriterBase {
  def handleResponse(
      handler: Option[CallbackHandler],
      response: Try[ErrorOr[Int]]
  ): Unit =
    handler match {
      // define callbacks if defined
      case Some(rh) =>
        response match {
          case Success(Right(code)) => rh.onSuccess(code)
          // application level issues
          case Success(Left(ex)) => rh.onApplicationFailure(ex)
          // connection/network level issues
          case Failure(ex) => rh.onNetworkFailure(ex)
        }
      case _ => ()
    }
}
