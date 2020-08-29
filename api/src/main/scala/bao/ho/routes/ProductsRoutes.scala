package bao.ho.routes

import bao.ho.conf.ProductConverterImpl
import bao.ho.models.Product
import cats.Applicative
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder, Header, InvalidMessageBodyFailure}
import bao.ho.repo.Repository
import org.http4s.{HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import cats.effect.Sync
import fs2.Stream
import io.circe.syntax._

final class ProductsRoutes[F[_]](repo: Repository[F])(implicit F: Sync[F]) extends Http4sDsl[F] {
  implicit def decodeProduct: EntityDecoder[F, Product]                    = jsonOf
  implicit def encodeProduct[A[_]: Applicative]: EntityEncoder[A, Product] = jsonEncoderOf
  private[routes] val prefixPath                                           = "/products"
  import cats.implicits._

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      val prefix = Stream.eval("[".pure[F])
      val suffix = Stream.eval("]".pure[F])
      val ps = repo
        .loadProducts()
        .groupAdjacentBy(_.productId)
        .map {
          case (_, rows) => ProductConverterImpl.fromDatabase(rows.toList)
        }
        .collect {
          case Some(p) => p
        }
        .map(_.asJson.noSpaces)
        .intersperse(",")
      @SuppressWarnings(Array("org.wartremover.warts.Any"))
      val result: Stream[F, String] = prefix ++ ps ++ suffix
      Ok(result, Header("Content-Type", "application/json"))
    case req @ PUT -> Root =>
      req
        .as[Product]
        .flatMap { p =>
          for {
            cnt <- repo.saveProduct(p)
            res <- cnt match {
                    case 0 => InternalServerError()
                    case _ => NoContent()
                  }
          } yield res
        }
        .handleErrorWith {
          case InvalidMessageBodyFailure(_, _) => BadRequest()
        }
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
}
