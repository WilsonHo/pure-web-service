import sbt._

object Dependencies {

  object Versions {
    val catsV         = "2.1.1"
    val meowV         = "0.4.1"
    val fs2V          = "2.2.2"
    val derevoV       = "0.10.5"
    val console4catsV = "0.8.1"
    val newtypeV      = "0.4.3"
    val refinedV      = "0.9.12"
    val monocleV      = "2.0.1"
    val squantsV      = "1.7.0"
    val http4sV       = "0.21.7"
    val circeV        = "0.12.3"
    val pureconfigV   = "0.13.0"
    val flywayV       = "6.5.5"
    val doobieV       = "0.9.0"
    val log4catsV     = "1.1.1"
    val log4jV        = "2.12.0"
  }

  import Versions._

  val flywayDependencies = Seq(
    "org.flywaydb" % "flyway-core" % flywayV
  )

  val pureconfigDependencies = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigV
  )

  val doobieDependencies = Seq(
    "org.tpolecat"     %% "doobie-core"        % doobieV,
    "org.tpolecat"     %% "doobie-postgres"    % doobieV,
    "org.tpolecat"     %% "doobie-refined"     % doobieV,
    "org.mariadb.jdbc" % "mariadb-java-client" % "2.5.4"
  )

  val circeDependencies = Seq(
    "io.circe" %% "circe-core"           % circeV,
    "io.circe" %% "circe-generic"        % circeV,
    "io.circe" %% "circe-refined"        % circeV,
    "io.circe" %% "circe-parser"         % circeV,
    "io.circe" %% "circe-generic"        % circeV,
    "io.circe" %% "circe-generic-extras" % "0.12.2"
  )

  val catsDependencies = Seq(
    "org.typelevel" %% "cats-core"   % catsV,
    "org.typelevel" %% "cats-effect" % catsV
  )

  val meowDependencies = Seq(
    "com.olegpy" %% "meow-mtl-core"    % meowV,
    "com.olegpy" %% "meow-mtl-effects" % meowV
  )

  val monocleDependencies = Seq(
    "com.github.julien-truffaut" %% "monocle-core"  % monocleV,
    "com.github.julien-truffaut" %% "monocle-macro" % monocleV
  )

  val derevoDependencies = Seq(
    "org.manatki" %% "derevo-cats"         % derevoV,
    "org.manatki" %% "derevo-cats-tagless" % derevoV
  )

  val fs2Dependencies = Seq(
    "co.fs2" %% "fs2-core" % fs2V
  )

  val console4catsDependencies = Seq(
    "dev.profunktor" %% "console4cats" % console4catsV
  )

  val newtypeDependencies = Seq("io.estatico" %% "newtype" % newtypeV)

  val refinedDependencies =
    Seq("eu.timepit" %% "refined" % refinedV, "eu.timepit" %% "refined-pureconfig" % refinedV)

  val squantsDependencies = Seq("org.typelevel" %% "squants" % squantsV)

  val http4sDependencies =
    Seq(
      "org.http4s" %% "http4s-core"         % http4sV,
      "org.http4s" %% "http4s-dsl"          % http4sV,
      "org.http4s" %% "http4s-circe"        % http4sV,
      "org.http4s" %% "http4s-blaze-server" % http4sV,
      "org.http4s" %% "http4s-blaze-client" % http4sV
    )

  val kindProjectDependencies = Seq(
    compilerPlugin(("org.typelevel" %% "kind-projector" % "0.11.0").cross(CrossVersion.full))
  )

  val contextAppliedDependencies = Seq(
    compilerPlugin("org.augustjune" %% "context-applied" % "0.1.2")
  )

//  val log4catsDependencies = Seq(
//    "io.chrisdavenport" %% "log4cats-core"  % log4catsV, // Only if you want to Support Any Backend
//    "io.chrisdavenport" %% "log4cats-slf4j" % log4catsV  // Direct Slf4j Support - Recommended
//  )

  val log4catsDependencies = Seq(
    "io.chrisdavenport" %% "log4cats-slf4j" % log4catsV // Direct Slf4j Support - Recommended
  )

  val log4jDependencies = Seq(
    "org.apache.logging.log4j" % "log4j-api"        % log4jV,
    "org.apache.logging.log4j" % "log4j-core"       % log4jV,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jV
  )
}
