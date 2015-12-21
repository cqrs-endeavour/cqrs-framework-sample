/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
name := "cqrs-framework-sample"

version := "0.1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "cqrs-framework" %% "cqrs-framework" % "0.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.12"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xlint",
  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-encoding", "UTF-8"
)

resolvers += "adebski at bintray" at "http://dl.bintray.com/adebski/maven"
