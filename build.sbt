lazy val root = (project in file(".")).
  settings(
    parallelExecution := false,
    organization := "com.example",
    scalaVersion := "2.11.8",
    version := "0.1.0-SNAPSHOT",
    name := "chess",
    libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.15",
    libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.3",
    libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.12.3",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.1" % Test,
    libraryDependencies += "org.mockito" % "mockito-core" % "2.7.5" % Test,
    libraryDependencies += "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.4" % Test
  )
