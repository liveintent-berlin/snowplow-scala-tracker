/*
 * Copyright (c) 2012-2016 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.snowplow.scalatracker
package emitters.sprayemitter

// Akka
import akka.actor.{ Actor, Props }
import akka.pattern.pipe

// Spray
import spray.client.pipelining._
import spray.http.HttpRequest
import spray.http.Uri.Query

/**
 * Primary actor, sending built request to collector and redirecting
 * responses to its child [[DispatcherActor]]
 */
class SenderActor(dispatcherBackoff: Int) extends Actor {
  import SprayEmitter.{ CollectorRequest, CollectorResponse }
  import SenderActor._

  import context.dispatcher

  val pipeline = sendReceive

  val responseDispatcher = context.actorOf(Props[DispatcherActor])

  def receive = {
    case CollectorRequest(attempt, request) => {
      val updatedRequest = updateSentTimestamp(request)
      val response = pipeline(updatedRequest).map(CollectorResponse(attempt + 1, _, request))
      response pipeTo responseDispatcher
    }
  }
}

object SenderActor {

  def props(dispatcherBackoff: Int): Props =
    Props(classOf[SenderActor], dispatcherBackoff)

  /**
   * Update failed HTTP request with new `stm` (dvce_sent_tstamp) key
   * It should be updated in last possible moment
   *
   * @param httpRequest existing HTTP request, probably already failed
   * @return HTTP request with updated sent timestamp
   */
  def updateSentTimestamp(httpRequest: HttpRequest): HttpRequest = {
    val queryString = httpRequest.uri.query.toMap ++ Map("stm" -> System.currentTimeMillis().toString)
    val updatedQuery = Query(queryString)
    val updatedUri = httpRequest.uri.copy(query = updatedQuery)
    httpRequest.copy(uri = updatedUri)
  }
}


