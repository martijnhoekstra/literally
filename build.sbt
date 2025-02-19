ThisBuild / tlBaseVersion := "1.1"

ThisBuild / developers += tlGitHubDev("mpilquist", "Michael Pilquist")
ThisBuild / startYear := Some(2021)

ThisBuild / crossScalaVersions := List("2.12.19", "2.13.14", "3.3.3")
ThisBuild / tlVersionIntroduced := Map("3" -> "1.0.2")

lazy val root = tlCrossRootProject.aggregate(core, tests)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .settings(
    name := "literally",
    scalacOptions := scalacOptions.value.filterNot(_ == "-source:3.0-migration"),
    libraryDependencies ++= {
      if (tlIsScala3.value) Nil else List("org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided)
    },
    tlMimaPreviousVersions := tlMimaPreviousVersions.value - "1.0.3"
  )
  .nativeSettings(
    tlVersionIntroduced := List("2.12", "2.13", "3").map(_ -> "1.1.0").toMap
  )

lazy val tests = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .enablePlugins(NoPublishPlugin)
  .dependsOn(core)
  .settings(
    name := "tests",
    scalacOptions := scalacOptions.value.filterNot(_ == "-source:3.0-migration"),
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0-RC1" % Test,
    libraryDependencies ++= {
      if (tlIsScala3.value) Nil else List("org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided)
    }
  )
