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
package com.snowplowanalytics.snowplow.scalatracker.emitters.sprayemitter

// Scala
import scala.concurrent.duration._

// Akka
import akka.actor.{Props, Actor}

/**
 * Actor responsible for decision about what to do with collector's response,
 * whether we need to forget about it or to schedule it.
 * We could be satisfied with single actor for sending and dispatching, but if
 * this one will crash, [[SenderActor]] will still be sending events to collector,
 * we only loose some of them.
 * Also we can implement some persistence for this actor.
 */
class DispatcherActor(backoffPeriod: Int) extends Actor {
  import SprayEmitter._

  import context.dispatcher

  def receive = {
    case CollectorResponse(attempt, response, _) if response.status.isFailure && attempt > 5 => ()
    case CollectorResponse(_, response, _) if response.status.isSuccess => ()
    case CollectorResponse(attempt, response, request) =>
      context.system.scheduler.scheduleOnce((attempt * backoffPeriod).milliseconds) {
        context.parent ! CollectorRequest(attempt, request)
      }
  }
}

object DispatcherActor {
  def props(backoff: Int): Props =
    Props(classOf[DispatcherActor], backoff)
}
