package bao.ho.repo

import java.util.UUID

//import bao.ho.DoobieRefinedExample.{City, ID}
import bao.ho.newtypes.NewTypes.{LanguageCode, ProductId, ProductName}
import bao.ho.models.Product
import bao.ho.repo.Repository.Result
import doobie.util.{Get, Put}
//import bao.ho.repo.Repository.Result
import cats.effect.Sync
//import doobie._
//import doobie.implicits._
//import doobie.postgres.implicits._
//import eu.timepit.refined.auto._
//import doobie.refined.implicits._
import fs2.Stream
import doobie._
import doobie.implicits._
import io.estatico.newtype.Coercible

final class DoobieRepository[F[_]: Sync](tx: Transactor[F]) extends Repository[F] {
  import doobie.refined.implicits._

  import doobie.util.compat.FactoryCompat

  private implicit def seqFactoryCompat[A]: FactoryCompat[A, List[A]] =
    FactoryCompat.fromFactor(List.iterableFactory)

  implicit def newTypePut[R, N](implicit ev: Coercible[Put[R], Put[N]], R: Put[R]): Put[N] = ev(R)

  implicit def newTypeRead[R, N](implicit ev: Coercible[Read[R], Read[N]], R: Read[R]): Read[N] =
    ev(R)

  implicit val uuidGet: Get[UUID] = Get[String].map(UUID.fromString)
  implicit val uuidPut: Put[UUID] = Put[String].contramap(_.toString)

  /**
    * Load a product from the database repository.
    *
    * @param id The unique ID of the product.
    * @return A list of database rows for a single product which you'll need to combine.
    */
  override def loadProduct(id: ProductId): F[List[Result]] =
    sql"""
        SELECT product.id, name.lang_code, name.name
        FROM product
        JOIN name ON product.id = name.product_id
        """
      .query[Result]
      .to[List]
      .transact(tx)

  /**
    * Load all products from the database repository.
    *
    * @return A stream of database rows which you'll need to combine.
    */
  override def loadProducts(): Stream[F, (ProductId, LanguageCode, ProductName)] =
    sql"""SELECT products.id, names.lang_code, names.name
        FROM products
        JOIN names ON products.id = names.product_id
        ORDER BY products.id"""
      .query[(ProductId, LanguageCode, ProductName)]
      .stream
      .transact(tx)

  /**
    * Save the given product in the database.
    *
    * @param p A product to be saved.
    * @return The number of affected database rows (product + translations).
    */
  override def saveProduct(p: Product): F[Int] = {
    val namesSql    = "INSERT INTO names (product_id, lang_code, name) VALUES (?, ?, ?)"
    val namesValues = p.names.map(t => (p.id, t.lang, t.name))
    val program = for {
      pi <- sql"INSERT INTO products (id) VALUES(${p.id})".update.run
      ni <- Update[(ProductId, LanguageCode, ProductName)](namesSql).updateMany(namesValues)
    } yield pi + ni
    program.transact(tx)
  }

  /**
    * Update the given product in the database.
    *
    * @param p The product to be updated.
    * @return The number of affected database rows.
    */
  override def updateProduct(p: Product): F[Int] = {
    val namesSql    = "INSERT INTO names (product_id, lang_code, name) VALUES (?, ?, ?)"
    val namesValues = p.names.map(t => (p.id, t.lang, t.name))
    val program = for {
      dl <- sql"DELETE FROM names WHERE product_id = ${p.id}".update.run
      ts <- Update[(ProductId, LanguageCode, ProductName)](namesSql).updateMany(namesValues)
    } yield dl + ts
    program.transact(tx)
  }
}
