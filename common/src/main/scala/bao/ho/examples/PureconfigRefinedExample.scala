package bao.ho.models

import com.typesafe.config.ConfigFactory
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import pureconfig._
import pureconfig.generic.semiauto._
//This one is very important
import eu.timepit.refined.pureconfig._

object PureconfigRefinedExample {
  final case class ApiConfig(host: NonEmptyString, port: PortNumber)

  object ApiConfig {
    implicit val configReader: ConfigReader[ApiConfig] = deriveReader[ApiConfig]
  }

  def main(args: Array[String]): Unit = {
    val m   = ConfigSource.default.load[ApiConfig]
    val cfg = ConfigFactory.load()
    // TODO Think about alternatives to `Throw`.
    val f = ConfigSource
      .fromConfig(cfg)
      .at("api")
      .loadOrThrow[ApiConfig]

    println(m)
    println(f)
  }
}
