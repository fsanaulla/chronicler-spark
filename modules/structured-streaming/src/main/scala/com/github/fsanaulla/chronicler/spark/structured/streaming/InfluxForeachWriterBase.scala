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
