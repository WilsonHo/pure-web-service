package bao.ho.conf

import bao.ho.newtypes.NewTypes.{DatabaseLogin, DatabasePassword, DatabaseUrl}
import eu.timepit.refined.types.string.NonEmptyString

final case class DatabaseConfig(
  driver: NonEmptyString,
  url: DatabaseUrl,
  user: DatabaseLogin,
  pass: DatabasePassword
)

object DatabaseConfig {}
