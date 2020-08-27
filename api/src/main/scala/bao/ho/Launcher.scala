package bao.ho

import cats.effect.{ExitCode, IO, IOApp}

object Launcher extends IOApp {

  def run(args: List[String]) =
    Http4sServer.stream[IO].compile.drain.as(ExitCode.Success)
}
