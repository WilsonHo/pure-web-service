package bao.ho

import bao.ho.conf.{ApiConfig, DatabaseConfig}
import bao.ho.db.FlywayDatabaseMigrator
import bao.ho.repo.DoobieRepository
import bao.ho.routes.{ProductRoutes, ProductsRoutes}
import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import com.typesafe.config.ConfigFactory
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import pureconfig.ConfigSource
import doobie.util.transactor.Transactor
import cats.implicits._
import eu.timepit.refined.auto._
import org.http4s.implicits._
import org.http4s.server.Router
import eu.timepit.refined.pureconfig._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext.global

object Http4sServer {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "scalafix:DisableSyntax.null"))
  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      migrator <- Stream.apply(new FlywayDatabaseMigrator).covary[F]
      cfg      <- Stream.apply(ConfigFactory.load(getClass.getClassLoader)).covary[F]
      apiConfig <- Stream
                    .apply(ConfigSource.fromConfig(cfg).at("api").loadOrThrow[ApiConfig])
                    .covary[F]
      dbConfig <- Stream
                   .apply(ConfigSource.fromConfig(cfg).at("database").loadOrThrow[DatabaseConfig])
                   .covary[F]
      _ <- Stream.eval(migrator.migrate(dbConfig.url, dbConfig.user, dbConfig.pass))
      tx = Transactor.fromDriverManager[F](
        dbConfig.driver.value,
        dbConfig.url.value,
        dbConfig.user.value,
        dbConfig.pass.value
      )
      repo           = new DoobieRepository(tx)
      productRoutes  = new ProductRoutes(repo)
      productsRoutes = new ProductsRoutes(repo)
      routes         = productRoutes.routes <+> productsRoutes.routes
      httpApp        = Router("/" -> routes).orNotFound
      finalHttpApp   = Logger.httpApp(true, true)(httpApp)
      exitCode <- BlazeServerBuilder[F](global)
                   .bindHttp(apiConfig.port, apiConfig.host)
                   .withHttpApp(finalHttpApp)
                   .serve
    } yield exitCode
  }.drain
}
