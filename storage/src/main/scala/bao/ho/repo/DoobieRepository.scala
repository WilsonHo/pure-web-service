package bao.ho.repo

import java.util.UUID

import bao.ho.newtypes.NewTypes.{LanguageCode, ProductId, ProductName}
import bao.ho.models.Product
import bao.ho.repo.Repository.Result
import doobie.util.{Get, Put}
import cats.effect.Sync
import fs2.Stream
import doobie._
import doobie.implicits._
import io.estatico.newtype.Coercible
import doobie.util.compat.FactoryCompat
import doobie.refined.implicits._

final class DoobieRepository[F[_]: Sync](tx: Transactor[F]) extends Repository[F] {

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
  override def loadProduct(id: ProductId): F[List[Result]] = {
    val query = sql"""
        SELECT product.id, name.lang_code, name.name
        FROM product
        JOIN name ON product.id = name.product_id
        WHERE product.id = ${id}::uuid
        """
    println(query)
    query
      .query[Result]
      .to[List]
      .transact(tx)
  }

  /**
    * Load all products from the database repository.
    *
    * @return A stream of database rows which you'll need to combine.
    */
  override def loadProducts(): Stream[F, Result] =
    sql"""SELECT product.id, name.lang_code, name.name
        FROM product
        JOIN name ON product.id = name.product_id
        ORDER BY product.id"""
      .query[Result]
      .stream
      .transact(tx)

  /**
    * Save the given product in the database.
    *
    * @param p A product to be saved.
    * @return The number of affected database rows (product + translations).
    */
  override def saveProduct(p: Product): F[Int] = {
    val namesSql =
      "INSERT INTO name (id, product_id, lang_code, name) VALUES (gen_random_uuid(), ?::uuid, ?, ?)"
    val namesValues = p.names.map(t => (p.id, t.lang, t.name))
    val program = for {
      pi <- sql"INSERT INTO product (id) VALUES(${p.id}::uuid)".update.run
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
    val namesSql =
      "INSERT INTO name (id, product_id, lang_code, name) VALUES (gen_random_uuid(), ?::uuid, ?, ?)"
    val namesValues = p.names.map(t => (p.id, t.lang, t.name))
    val program = for {
      dl <- sql"DELETE FROM name WHERE product_id = ${p.id}::uuid".update.run
      ts <- Update[(ProductId, LanguageCode, ProductName)](namesSql).updateMany(namesValues)
    } yield dl + ts
    program.transact(tx)
  }
}
