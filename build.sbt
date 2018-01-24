/*
 * Copyright (c) 2013-2017 Snowplow Analytics Ltd. All rights reserved.
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
lazy val root = project.in(file("."))
  .settings(Seq[Setting[_]](
    organization       := "com.snowplowanalytics",
    version            := "0.5.0-M2-li",
    description        := "Scala tracker for Snowplow",
    name               := "snowplow-scala-tracker",
    scalaVersion       := "2.11.12",
    scalacOptions      := Seq("-deprecation", "-encoding", "utf8"),
    javacOptions       ++= Seq("-source", "1.8", "-target", "1.8")
  ))
  .settings(BuildSettings.buildSettings)
  .settings(Seq(
    shellPrompt := { _ => name.value + " > " }
  ))
  .settings(
    libraryDependencies := Seq(
      Dependencies.Libraries.scalajHttp,
      Dependencies.Libraries.json4sJackson,
      Dependencies.Libraries.igluCore,
      Dependencies.Libraries.igluCoreJson4s,
      Dependencies.Libraries.mockito,
      Dependencies.Libraries.specs2,
      Dependencies.Libraries.scalaCheck)
  )
