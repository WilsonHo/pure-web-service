package bao.ho.models

import bao.ho.CoercibleCodecs
import bao.ho.newtypes.NewTypes.{LanguageCode, ProductName}
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class Translation(lang: LanguageCode, name: ProductName)

object Translation extends CoercibleCodecs {
  import io.circe.refined._

  implicit val decoder: Decoder[Translation] = deriveDecoder[Translation]
  implicit val encoder: Encoder[Translation] = deriveEncoder[Translation]
}
