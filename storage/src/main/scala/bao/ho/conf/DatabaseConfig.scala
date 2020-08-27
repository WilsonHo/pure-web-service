package bao.ho.conf

import bao.ho.newtypes.NewTypes.{DatabaseLogin, DatabasePassword, DatabaseUrl}
import eu.timepit.refined.types.string.NonEmptyString
//import pureconfig.ConfigReader
//import pureconfig.generic.semiauto._
//import pureconfig.generic.auto._

final case class DatabaseConfig(
  driver: NonEmptyString,
  url: DatabaseUrl,
  user: DatabaseLogin,
  pass: DatabasePassword
)

object DatabaseConfig {
//  implicit val configReader: ConfigReader[DatabaseConfig] =
//    deriveReader[DatabaseConfig]
}
