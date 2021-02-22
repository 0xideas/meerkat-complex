val Http4sVersion = "0.21.19"
val CirceVersion = "0.12.3"
val Specs2Version = "4.10.0"
val LogbackVersion = "1.2.3"
val BreezeVersion = "1.1"
val Log4jversion = "2.14.0"

lazy val root = (project in file("."))
  .settings(
    organization := "com.github.leontl",
    name := "meerkat",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.11",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",

      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.apache.logging.log4j" % "log4j-core" % Log4jversion,
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % Log4jversion,

      "org.scalacheck" %% "scalacheck" % "1.14.1",
      "com.github.haifengl" %% "smile-scala" % "2.5.3",
      "org.clapper" %% "grizzled-slf4j" % "1.3.4",
      //"com.stripe" %% "rainier-core" % "0.3.3",

      "org.scalanlp" %% "breeze" % BreezeVersion,
      "org.scalanlp" %% "breeze-natives" % BreezeVersion,
      "org.scalanlp" %% "breeze-viz" % BreezeVersion,
      
      "org.bytedeco" % "javacpp"   % "1.5.4" classifier "linux-x86_64",
      "org.bytedeco" % "openblas"  % "0.3.10-1.5.4" classifier "linux-x86_64",
      "org.bytedeco" % "arpack-ng" % "3.7.0-1.5.4" classifier "linux-x86_64",

      "com.microsoft.onnxruntime" % "onnxruntime" % "1.5.2"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )


enablePlugins(JavaAppPackaging)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)


