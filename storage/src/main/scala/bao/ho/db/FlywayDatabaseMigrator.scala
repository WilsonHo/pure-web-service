package bao.ho.db
import bao.ho.newtypes.NewTypes.{DatabaseLogin, DatabasePassword, DatabaseUrl}
import cats.effect.Sync
import org.flywaydb.core.Flyway

final class FlywayDatabaseMigrator[F[_]](implicit F: Sync[F]) extends DatabaseMigrator[F] {

  override def migrate(url: DatabaseUrl, user: DatabaseLogin, pass: DatabasePassword): F[Int] =
    F.delay {
      val flyway: Flyway = Flyway
        .configure()
        .dataSource(url.value, user.value, pass.value)
        .load()
      flyway.migrate()
    }
}
