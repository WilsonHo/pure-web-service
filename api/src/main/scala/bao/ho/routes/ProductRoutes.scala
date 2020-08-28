package bao.ho.routes

import bao.ho.models.Product
import bao.ho.newtypes.NewTypes.ProductId
import bao.ho.repo.Repository
import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import eu.timepit.refined.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.Router

final class ProductRoutes[F[_]](repo: Repository[F])(implicit F: Sync[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath                                           = "/product"
  implicit def decodeProduct: EntityDecoder[F, Product]                    = jsonOf
  implicit def encodeProduct[A[_]: Applicative]: EntityEncoder[A, Product] = jsonEncoderOf

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / UUIDVar(id) =>
      for {
        rows <- repo.loadProduct(ProductId(id))
        resp <- Product.fromDatabase(rows).fold(NotFound())(p => Ok(p))
      } yield resp
    case req @ PUT -> Root / UUIDVar(_) =>
      req
        .as[Product]
        .flatMap { p =>
          for {
            cnt <- repo.updateProduct(p)
            res <- cnt match {
                    case 0 => NotFound()
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
