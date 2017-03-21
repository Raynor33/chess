lazy val root = (project in file(".")).
  settings(
    organization := "com.example",
    scalaVersion := "2.12.1",
    version := "0.1.0-SNAPSHOT",
    name := "chess",
    libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.1",
    libraryDependencies += "org.mockito" % "mockito-core" % "2.7.5" % Test


  )
