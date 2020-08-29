package bao.ho.conf

import bao.ho.models.{Product, Translation}
import bao.ho.repo.Repository.Result
import cats.data.NonEmptyList

trait ProductConverter {

  /**
    * Try to create a Product from the given list of database rows.
    *
    * @param rows The database rows describing a product and its translations.
    * @return An option to the successfully created Product.
    */
  def fromDatabase(rows: List[Result]): Option[Product] =
    rows
      .groupBy(_.productId)
      .view
      .mapValues(_.map(r => Translation(lang = r.langCode, name = r.productName)))
      .mapValues(NonEmptyList.fromListUnsafe)
      .headOption
      .map(t => (Product.apply _).tupled(t))
}

object ProductConverterImpl extends ProductConverter
