import sbt._
import sbt.Keys._

object Build extends Build {

  lazy val yet_another_ltsv_scala = Project(
    id = "yet_another_ltsv_scala",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "yet_another_ltsv_scala",
      organization := "me.masahito",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      libraryDependencies ++= Seq(
        // test
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      )
      // add other settings here
    )
  )
}
