lazy val root = (project in file(".")).
  settings(
    parallelExecution := false,
    organization := "com.example",
    scalaVersion := "2.11.8",
    version := "0.1.0-SNAPSHOT",
    name := "chess",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.15",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.1" % Test,
    libraryDependencies += "org.mockito" % "mockito-core" % "2.7.5" % Test
  )
