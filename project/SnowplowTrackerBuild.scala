/* 
 * Copyright (c) 2013-2016 Snowplow Analytics Ltd. All rights reserved.
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
import sbt._
import Keys._

import com.liveintent.sbt.LiveIntentPlugin

object SnowplowTrackerBuild extends Build {

  import Dependencies._
  import BuildSettings._

  // Configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  // Define our project, with basic project information and library dependencies
  lazy val project = Project("snowplow-scala-tracker", file("."))
    .enablePlugins(LiveIntentPlugin)
    .settings(buildSettings: _*)
    .settings(
      libraryDependencies <++= Dependencies.onVersion(
        all = Seq(
        Libraries.commonsLang,
        Libraries.commonsCodec,
        Libraries.jodaTime,
        Libraries.jodaConvert,
        Libraries.jodaMoney,
        Libraries.jackson,
        Libraries.json4sJackson,
        Libraries.sprayClient,
        Libraries.akka,
        Libraries.mockito,
        Libraries.sprayTest),
        on29 = Seq(Libraries.collUtil._29, Libraries.specs2._29),
        on210 = Seq(Libraries.collUtil._210, Libraries.specs2._210),
        on211 = Seq(Libraries.collUtil._211, Libraries.specs2._211)
      )
    )
}
