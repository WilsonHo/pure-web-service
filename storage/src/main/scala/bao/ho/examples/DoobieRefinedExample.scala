package bao.ho

//import java.util.UUID

import java.util.UUID

import cats.effect.IO
import doobie.util.compat.FactoryCompat
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import doobie._
import doobie.implicits._
import io.estatico.newtype.Coercible
import io.estatico.newtype.macros.newtype
import doobie.refined.implicits._
import doobie.util.{Get, Put}
import eu.timepit.refined.auto._
import eu.timepit.refined.string.Uuid

import scala.concurrent.ExecutionContext

object DoobieRefinedExample {
  //  very important

  // Remove the implicit error from InteliJ
  private implicit def seqFactoryCompat[A]: FactoryCompat[A, List[A]] =
    FactoryCompat.fromFactor(List.iterableFactory)

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/pure",
    "postgres",
    "postgres"
  )
  implicit val uuidGet: Get[UUID] = Get[String].map(UUID.fromString)
  implicit val uuidPut: Put[UUID] = Put[String].contramap(_.toString)

  //  import doobie.postgres._
  //  import doobie.postgres.implicits._
  implicit def newTypePut[R, N](implicit ev: Coercible[Put[R], Put[N]], R: Put[R]): Put[N] = ev(R)

  implicit def newTypeRead[R, N](implicit ev: Coercible[Read[R], Read[N]], R: Read[R]): Read[N] =
    ev(R)

  type CId = String Refined Uuid
  @newtype case class ID(value: CId)
  type CName = String Refined NonEmpty
  final case class City(id: ID, name: CName)
  //  final case class A private (private val id: Int, name: String)

  def findCName(id: ID) =
    sql"""select id, name from name WHERE id = $id::uuid"""
      .query[City]
      .to[List]

  def find(n: ID) =
    sql"select id, name from city where id = '$n'".query[City].option

  def findAll() =
    sql"select id, name from city".query[City].to[List]

  def findAllTuple() =
    sql"select id, name from city".query[(CId, CName)].to[List]

  def main(args: Array[String]): Unit =
    //    val id: Int Refined Positive = 1
    //    val m = A(1, "dsa")
    //    println(m)
    (for {
      _ <- findCName(ID("a7db3821-f254-49c1-98ea-ab8584877fa8"))
            .transact(xa)
            .map(println)
      //      _ <- findCName(ID(1))
      //            .transact(xa)
      //            .map(println)
      //      _ <- findAll()
      //            .transact(xa)
      //            .map(println)
      //      _ <- findAllTuple()
      //            .transact(xa)
      //            .map(println)
    } yield ())
      .unsafeRunSync()
}
