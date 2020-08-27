package bao.ho.newtypes

import java.util.UUID

import io.estatico.newtype.macros._
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string.MatchesRegex

object NewTypes {
  type LanguageCodePred = String Refined MatchesRegex[W.`"^[a-z]{2}$"`.T]
  type ProductNamePred  = String Refined NonEmpty
  type DatabaseUrl      = String Refined NonEmpty
  type DatabaseLogin    = String Refined NonEmpty
  type DatabasePassword = String Refined NonEmpty
  @newtype case class LanguageCode(value: LanguageCodePred)
  @newtype case class ProductName(value: ProductNamePred)
  @newtype case class ProductId(value: UUID)
}
