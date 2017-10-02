// *** Settings ***

useGpg := false

lazy val commonSettings = Seq(
  organization := "org.scala-rules",
  organizationHomepage := Some(url("https://github.com/scala-rules")),
  homepage := Some(url("https://github.com/scala-rules/finance-dsl")),
  version := "0.1.3-SNAPSHOT",
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.11.11", "2.12.3"),
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-Xfatal-warnings")
) ++ staticAnalysisSettings ++ publishSettings


// *** Projects ***

lazy val financeDslRoot = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "finance-dsl",
    description := "Finance DSL",
    libraryDependencies ++= dependencies
  )


// *** Dependencies ***

lazy val dependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
)

// *** Static analysis ***

lazy val staticAnalysisSettings = {
  lazy val compileScalastyle = taskKey[Unit]("Runs Scalastyle on production code")
  lazy val testScalastyle = taskKey[Unit]("Runs Scalastyle on test code")

  Seq(
    scalastyleConfig in Compile := (baseDirectory in ThisBuild).value / "project" / "scalastyle-config.xml",
    scalastyleConfig in Test := (baseDirectory in ThisBuild).value / "project" / "scalastyle-test-config.xml",

    // The line below is needed until this issue is fixed: https://github.com/scalastyle/scalastyle-sbt-plugin/issues/44
    scalastyleConfig in scalastyle := (baseDirectory in ThisBuild).value / "project" / "scalastyle-test-config.xml",

    compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
    testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
  )
}

addCommandAlias("verify", ";compileScalastyle;testScalastyle;coverage;+test;coverageReport")
addCommandAlias("release", ";clean;+compile;+publishSigned")


// *** Publishing ***

lazy val publishSettings = Seq(
  pomExtra := pom,
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  }
)

lazy val pom =
  <developers>
    <developer>
      <name>Jan-Hendrik Kuperus</name>
      <email>jan-hendrik@scala-rules.org</email>
      <organization>Yoink Development</organization>
      <organizationUrl>http://www.yoink.nl</organizationUrl>
    </developer>
    <developer>
      <name>Jan Ouwens</name>
      <email>jan.ouwens@gmail.com</email>
      <organization>CodeStar</organization>
      <organizationUrl>https://codestar.nl/</organizationUrl>
    </developer>
    <developer>
      <name>Nathan Perdijk</name>
      <email>nathan@scala-rules.org</email>
      <organization>CodeStar</organization>
      <organizationUrl>https://codestar.nl/</organizationUrl>
    </developer>
    <developer>
      <name>Pim Verkerk</name>
      <email>pimverkerk@hotmail.com</email>
      <organization>CodeStar</organization>
      <organizationUrl>https://codestar.nl/</organizationUrl>
    </developer>
    <developer>
      <name>Vincent Zorge</name>
      <email>scala-rules@linuse.nl</email>
      <organization>Linuse</organization>
      <organizationUrl>https://github.com/vzorge</organizationUrl>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:git@github.com:scala-rules/finance-dsl.git</connection>
    <developerConnection>scm:git:git@github.com:scala-rules/finance-dsl.git</developerConnection>
    <url>git@github.com:scala-rules/finance-dsl.git</url>
  </scm>
  
