import sbt._
import sbt.Keys._

object YetAnotherLTSVScalaProject extends Build {

  lazy val yet_another_ltsv_scala = Project(
    id = "yet_another_ltsv_scala",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "yet_another_ltsv_scala",
      organization := "me.masahito",
      version := "0.0.1",
      crossScalaVersions := Seq("2.9.2", "2.10.0"),
      libraryDependencies ++= Seq(
        // test
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      ),

      // add other settings here

      sbtPlugin := false,
      scalacOptions ++= Seq("-unchecked"),
      publishMavenStyle := true,
      publishTo <<= version { (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false },
      pomExtra :=
        <url>https://github.com/masahitojp/yet-another-ltsv-scala</url>
        <licenses>
          <license>
            <name>Apache License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:masahitojp/yet-another-ltsv-scala.git</url>
          <connection>scm:git:git@github.com:masahitojp/yet-another-ltsv-scala.git</connection>
        </scm>
        <developers>
          <developer>
            <id>masahito</id>
            <name>Nakamura Masato</name>
            <url>http://masahito.me/</url>
          </developer>
        </developers>
    )
  )
}
