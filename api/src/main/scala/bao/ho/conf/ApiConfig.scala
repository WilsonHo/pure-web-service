package bao.ho.conf

import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._
import eu.timepit.refined.auto._
import eu.timepit.refined.pureconfig._
//import eu.timepit.refined.pureconfig._ very important
final case class ApiConfig(host: NonEmptyString, port: PortNumber)

object ApiConfig {
  implicit val configReader: ConfigReader[ApiConfig] = deriveReader[ApiConfig]
}
