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
package emitters
package sprayemitter

// akka
import akka.actor.{ActorSystem, Props}

// spray
import spray.http._
import spray.httpx.RequestBuilding._

// HOCON
import com.typesafe.config.ConfigFactory

/**
 * Main emitter class, responsible for receiving payloads, holding a reference
 * to [[ActorSystem]] and underlying utilitary actors
 *
 * @param initialBackoff initial backoff period in ms, doubling after each failure
 */
class SprayEmitter(system: ActorSystem, host: String, port: Int, initialBackoff: Int) extends TEmitter {
  import SprayEmitter._

  val senderRef = system.actorOf(SenderActor.props(initialBackoff))

  def input(event: Map[String, String]): Unit = {
    senderRef ! buildRequest(event, 0)
  }

  /**
   * Build initial HTTP request
   * All subsequent retrying requests can be derived from this one
   *
   * @param payload
   * @param attempt
   * @return
   */
  def buildRequest(payload: Map[String, String], attempt: Int): CollectorRequest = {
    val httpRequest = constructGetRequest(host, payload, port)
    CollectorRequest(attempt, httpRequest)
  }
}

object SprayEmitter {
  lazy implicit val system = ActorSystem(
    generated.ProjectSettings.name,
    ConfigFactory.parseString("akka.daemonic=on"))

  case class CollectorResponse(attempt: Int, httpResponse: HttpResponse, request: HttpRequest)

  case class CollectorRequest(attempt: Int, httpRequest: HttpRequest)

  def apply(host: String): SprayEmitter =
    new SprayEmitter(system, host, 80, 2000)

  def apply(host: String, port: Int): SprayEmitter =
    new SprayEmitter(system, host, port, 2000)

  def apply(system: ActorSystem, host: String, port: Int, initialBackoff: Int): SprayEmitter =
    new SprayEmitter(system, host, port, initialBackoff)

  /**
   * Construct GET request with single event payload
   *
   * @param host URL host (not header)
   * @param payload map of event keys
   * @param port URL port (not header)
   * @return HTTP request with event
   */
  def constructGetRequest(host: String, payload: Map[String, String], port: Int): HttpRequest = {
    val uri = Uri()
      .withScheme("http")
      .withPath(Uri.Path("/i"))
      .withAuthority(Uri.Authority(Uri.Host(host), port))
      .withQuery(payload)
    Get(uri)
  }
}
