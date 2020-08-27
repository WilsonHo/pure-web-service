package bao.ho.db

import bao.ho.newtypes.NewTypes.{DatabaseLogin, DatabasePassword, DatabaseUrl}

trait DatabaseMigrator[F[_]] {
  def migrate(url: DatabaseUrl, user: DatabaseLogin, pass: DatabasePassword): F[Int]
}
