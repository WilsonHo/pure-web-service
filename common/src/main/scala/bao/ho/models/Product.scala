package bao.ho.models

import bao.ho.CoercibleCodecs
import bao.ho.newtypes.NewTypes.ProductId
import cats.data.NonEmptyList
import io.circe.{Decoder, Encoder}
//import io.circe.generic.semiauto._
import io.circe.generic.semiauto._
//import cats.Order

final case class Product(id: ProductId, names: NonEmptyList[Translation])

object Product extends CoercibleCodecs {
//  import io.circe.refined._ very important

  def fromDatabase[T](rows: List[T]): Option[Product] = ???
//  {
//    val po = for {
//      (id, c, n) <- rows.headOption
//      t = Translation(lang = c, name = n)
//      p <- Product(id = id, names = NonEmptySet.one(t)).some
//    } yield p
//    po.map(
//      p =>
//        rows.drop(1).foldLeft(p) { (a, cols) =>
//          val (id, c, n) = cols
//          a.copy(names = a.names.add(Translation(lang = c, name = n)))
//        }
//    )
//  }

  implicit val decode: Decoder[Product] = deriveDecoder[Product]

  implicit val encode: Encoder[Product] = deriveEncoder[Product]

//  implicit val order: Order[Product] = ???
//  new Order[Product] {
//    def compare(x: Product, y: Product): Int = x.id.value.compare(y.id.value)
//  }
}
