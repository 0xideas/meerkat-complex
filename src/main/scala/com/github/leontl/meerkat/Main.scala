package com.github.leontl.meerkat

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    MeerkatServer.stream[IO].compile.drain.as(ExitCode.Success)
}