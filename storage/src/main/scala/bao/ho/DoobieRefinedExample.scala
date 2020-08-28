package bao.ho

import cats.effect.IO
import doobie.util.compat.FactoryCompat
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import doobie._
import doobie.implicits._
import scala.concurrent.ExecutionContext

object DoobieRefinedExample {
//  very important
  import doobie.refined.implicits._

  // Remove the implicit error from InteliJ
  private implicit def seqFactoryCompat[A]: FactoryCompat[A, List[A]] =
    FactoryCompat.fromFactor(List.iterableFactory)

  implicit val cs = IO.contextShift(ExecutionContext.global)

  val xa = Transactor.fromDriverManager[IO](
    "org.mariadb.jdbc.Driver",
    "jdbc:mysql://localhost:3306/doobie",
    "root",
    "root"
  )

  type CId   = Int Refined Positive
  type CName = String Refined NonEmpty
  final case class City(id: CId, name: CName)
//  final case class A private (private val id: Int, name: String)

  def findCName(n: Int) =
    sql"select name from city where id = $n".query[CName].option

  def find(n: Int) =
    sql"select id, name from city where id = $n".query[City].option

  def findAll() =
    sql"select id, name from city".query[City].to[List]

  def main(args: Array[String]): Unit =
//    val m = A(1, "dsa")
//    println(m)
    (for {
      _ <- find(1)
            .transact(xa)
            .map(println)
      _ <- findCName(1)
            .transact(xa)
            .map(println)
      _ <- findAll()
            .transact(xa)
            .map(println)
    } yield ())
      .unsafeRunSync()
}
