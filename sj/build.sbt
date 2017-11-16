scalaVersion := "2.11.11"

// shadow sbt-scalajs crossProject and CrossType until scala.js 1.0.0 is released
import sbtcrossproject.{crossProject, CrossType}

// project wide settings
val projectWideSettings = Seq(
  scalaVersion := "2.11.11", 
  organization := "com.vkhristenko",
  version := "0.0.0")

// top project
val topSettings = Seq(
  name := "sj")

lazy val test = 
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Full) // [Pure, Full, Dummy], default CrossType.Full
    .settings(
      projectWideSettings,
      topSettings)
//    .aggregate(io)
//    .dependsOn(io)
    .jsSettings(/* ... */)
    .jvmSettings(/* ... */)
    .nativeSettings(/* ... */)
    .in(file("."))

lazy val testJS = test.js
lazy val testJVM = test.jvm
lazy val testNative = test.native
