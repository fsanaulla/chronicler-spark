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

final case class CallbackHandler(onSuccess: Int => Unit,
                                 onApplicationFailure: Throwable => Unit,
                                 onNetworkFailure: Throwable => Unit)

object CallbackHandler {
  val empty = CallbackHandler(_ => {}, _ => {}, _ => {})

  def withSuccess(f: Int => Unit): CallbackHandler = empty.copy(onSuccess = f)
  def withAppFailure(f: Throwable => Unit): CallbackHandler = empty.copy(onApplicationFailure = f)
  def withNetworkFailure(f: Throwable => Unit): CallbackHandler = empty.copy(onNetworkFailure = f)
}