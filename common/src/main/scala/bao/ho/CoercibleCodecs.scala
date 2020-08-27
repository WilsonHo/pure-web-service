package bao.ho

import io.circe.{Decoder, Encoder, KeyDecoder, KeyEncoder}
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._

trait CoercibleCodecs {

  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.repr.asInstanceOf[B])

  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]: KeyDecoder[A] =
    KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]: KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.repr.asInstanceOf[B])
}
