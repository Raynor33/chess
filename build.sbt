name := "chess"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
    jdbc,
    cache,
    ws,
    "com.typesafe.play" %% "play-json" % "2.5.15",
    "org.julienrf" % "play-json-derived-codecs_2.11" % "3.3",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.12.3",
    "org.reactivemongo" %% "reactivemongo" % "0.12.3",
    "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % Test,
    "org.mockito" % "mockito-core" % "2.7.5" % Test,
    "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.4" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

disablePlugins(PlayLayoutPlugin)

PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
