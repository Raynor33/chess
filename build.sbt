import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "chess",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % Test,
    libraryDependencies += "org.mockito" % "mockito-core" % "2.7.5" % Test


  )
