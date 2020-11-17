package com.github.leontl.meerkat

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._


final case class Greeting(greeting: String) extends AnyVal
object Greeting {
  implicit val greetingEncoder: Encoder[Greeting] = new Encoder[Greeting] {
    final def apply(a: Greeting): Json = Json.obj(
      ("message", Json.fromString(a.greeting)),
    )
  }
  implicit def greetingEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Greeting] =
    jsonEncoderOf[F, Greeting]
}

trait MeerkatAction[F[_]]{
  def hello(n: MeerkatAction.Name): F[Greeting]
}

object MeerkatAction {
  implicit def apply[F[_]](implicit ev: MeerkatAction[F]): MeerkatAction[F] = ev

  final case class Name(name: String) extends AnyVal

  def impl[F[_]: Applicative]: MeerkatAction[F] = new MeerkatAction[F]{
    def hello(n: MeerkatAction.Name): F[Greeting] =
        Greeting("Do " + n.name + "!").pure[F]
  }
}