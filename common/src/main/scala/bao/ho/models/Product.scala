package bao.ho.models

import bao.ho.CoercibleCodecs
import bao.ho.newtypes.NewTypes.ProductId
import cats.data.NonEmptyList
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class Product(id: ProductId, names: NonEmptyList[Translation])

object Product extends CoercibleCodecs {
  implicit val decode: Decoder[Product] = deriveDecoder[Product]

  implicit val encode: Encoder[Product] = deriveEncoder[Product]
}
