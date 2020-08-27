package bao.ho.routes

//import bao.ho.models.Product
import java.util.UUID

import bao.ho.newtypes.NewTypes.ProductId
import bao.ho.repo.Repository
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import cats.effect.Sync
//import cats.implicits._
//import eu.timepit.refined.auto._
//import fs2.Stream
//import io.circe.syntax._
//import org.http4s._
//import org.http4s.circe._
//import org.http4s.dsl._

final class ProductsRoutes[F[_]](repo: Repository[F])(implicit F: Sync[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/products"
//  implicit def decodeProduct     = jsonOf
//  implicit def encodeProduct     = jsonEncoderOf

  import cats.syntax.functor._
  import cats.syntax.flatMap._

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
      for {
        m  <- repo.loadProduct(ProductId(UUID.randomUUID()))
        x  = m.head._3.value.value
        rs <- Ok(x)
      } yield rs

//        .map(_.head._3.value.value)
//        .map(x => )
  }

  //      val prefix = Stream.eval("[".pure[F])
//      val suffix = Stream.eval("]".pure[F])
//      val ps = repo.loadProducts
//        .groupAdjacentBy(_._1.value)
//        .map {
//          case (id, rows) => Product.fromDatabase(rows.toList)
//        }
//        .collect {
//          case Some(p) => p
//        }
//        .map(_.asJson.noSpaces)
//        .intersperse(",")
//      @SuppressWarnings(Array("org.wartremover.warts.Any"))
//      val result: Stream[F, String] = prefix ++ ps ++ suffix
//      Ok(result)
//    case PUT -> Root => ???
//      req
//        .as[Product]
//        .flatMap { p =>
//          for {
//            cnt <- repo.saveProduct(p)
//            res <- cnt match {
//                    case 0 => InternalServerError()
//                    case _ => NoContent()
//                  }
//          } yield res
//        }
//        .handleErrorWith {
//          case InvalidMessageBodyFailure(_, _) => BadRequest()
//        }
//  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
}
