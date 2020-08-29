package bao.ho.repo

import bao.ho.newtypes.NewTypes.{LanguageCode, ProductId, ProductName}
import bao.ho.models.Product
import bao.ho.repo.Repository.Result
import fs2._

trait Repository[F[_]] {
  def loadProduct(id: ProductId): F[List[Result]]

  def loadProducts(): Stream[F, Result]

  def saveProduct(p: Product): F[Int]

  def updateProduct(p: Product): F[Int]
}

object Repository {
  case class Result(productId: ProductId, langCode: LanguageCode, productName: ProductName)
}
