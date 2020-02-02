scalaVersion := "2.13.0"

val cats = "2.0.0"

libraryDependencies ++= Seq("org.typelevel" %% "cats-core").map(_ % cats)
// scalacOptions ++= Seq("-Xfatal-warnings")
