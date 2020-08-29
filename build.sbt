import Dependencies._

lazy val commonSettings = Seq(
  organization := "bao.ho",
  version := "0.1",
  scalaVersion := "2.13.3",
  scalacOptions ++= Seq("-Ymacro-annotations", "-Xfatal-warnings")
)

lazy val root = (project in file("."))
  .settings(name := "pure-web-service")

lazy val common = (project in file("common"))
  .settings(name := "common")
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= catsDependencies)
  .settings(libraryDependencies ++= log4catsDependencies)
  .settings(libraryDependencies ++= log4jDependencies)
  .settings(libraryDependencies ++= pureconfigDependencies)
  .settings(libraryDependencies ++= meowDependencies)
  .settings(libraryDependencies ++= circeDependencies)
  .settings(libraryDependencies ++= flywayDependencies)
  .settings(libraryDependencies ++= monocleDependencies)
  .settings(libraryDependencies ++= derevoDependencies)
  .settings(libraryDependencies ++= fs2Dependencies)
  .settings(libraryDependencies ++= console4catsDependencies)
  .settings(libraryDependencies ++= newtypeDependencies)
  .settings(libraryDependencies ++= refinedDependencies)
  .settings(libraryDependencies ++= squantsDependencies)
  .settings(libraryDependencies ++= kindProjectDependencies)
  .settings(libraryDependencies ++= contextAppliedDependencies)
  .enablePlugins(ScalafmtPlugin)

lazy val storage = (project in file("storage"))
  .settings(name := "storage")
  .settings(commonSettings: _*)
  .dependsOn(common % "compile->compile;test->test")
  .settings(libraryDependencies ++= doobieDependencies)

lazy val service = (project in file("service"))
  .settings(name := "service")
  .settings(commonSettings: _*)
  .dependsOn(storage % "compile->compile;test->test")

lazy val api = (project in file("api"))
  .settings(name := "api")
  .settings(commonSettings: _*)
  .dependsOn(service % "compile->compile;test->test")
  .settings(libraryDependencies ++= http4sDependencies)
