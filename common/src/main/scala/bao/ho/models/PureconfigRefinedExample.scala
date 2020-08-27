package bao.ho.models

import com.typesafe.config.ConfigFactory
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString

//import bao.ho.CoercibleCodecs
//import eu.timepit.refined.api._
//import eu.timepit.refined.auto._
//import eu.timepit.refined.collection._
//import io.circe._
//import io.circe.generic.semiauto._
//import io.circe.parser._
//import io.circe.syntax._
//import io.estatico.newtype.macros.newtype

import pureconfig._
import pureconfig.generic.semiauto._
//import eu.timepit.refined.auto._
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

  //  object Models {
//    type RecipientRules   = NonEmpty
//    type MessageBodyRules = NonEmpty
//
//    type RecipientType   = String Refined RecipientRules
//    type MessageBodyType = String Refined MessageBodyRules
//
//    @newtype case class Recipient(value: RecipientType)
//
//    @newtype case class MessageBody(value: MessageBodyType)
//
//    case class Message(recipient: Recipient, body: MessageBody)
//  }
//
//  object Codecs extends CoercibleCodecs {
//    import Models._
//
//    implicit val messageEncoder: Encoder[Message] = deriveEncoder[Message]
//    implicit val messageDecoder: Decoder[Message] = deriveDecoder[Message]
//  }
//
//  def main(args: Array[String]): Unit = {
//    import Codecs._
//    import Models._
//
//    val recipient = Recipient("@marcin")
//    val body      = MessageBody("How are you?")
//
////    val id    = LanguageCode("vn")
////    val names = ProductName("Translation")
////    val p     = Translation(id, names)
////    println(p)
//    val msg = Message(recipient, body)
////
//    val encodedMsg = msg.asJson.noSpaces
//    // {"recipient":"@marcin","body":"How are you?"}
//    println(body)
//
//    println(decode[Message](encodedMsg))
//    // Right(Message(marcin,How are you?))
//
//    println(decode[Message]("""{ "recipient": "", "body": "How are you?" }"""))
////     Left(DecodingFailure(Predicate isEmpty() did not fail., List(DownField(recipient))))
//  }
}
