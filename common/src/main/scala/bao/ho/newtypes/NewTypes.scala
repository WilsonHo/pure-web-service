package bao.ho.newtypes

import java.util.UUID

import cats.Eq
import io.estatico.newtype.macros._
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string.MatchesRegex
import io.estatico.newtype.Coercible

object NewTypes {
  type LanguageCodePred = String Refined MatchesRegex[W.`"^[a-z]{2}$"`.T]
  type ProductNamePred  = String Refined NonEmpty
  type DatabaseUrl      = String Refined NonEmpty
  type DatabaseLogin    = String Refined NonEmpty
  type DatabasePassword = String Refined NonEmpty
  @newtype case class LanguageCode(value: LanguageCodePred)
  @newtype case class ProductName(value: ProductNamePred)
  @newtype case class ProductId(value: UUID)

  implicit def coercibleEq[R, N](implicit ev: Coercible[Eq[R], Eq[N]], R: Eq[R]): Eq[N] =
    ev(R)
}
