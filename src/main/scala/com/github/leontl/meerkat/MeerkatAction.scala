package com.github.leontl.meerkat

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._


final case class Action(action: String) extends AnyVal

object Action {
  implicit val actionEncoder: Encoder[Action] = new Encoder[Action] {
    final def apply(a: Action): Json = Json.obj(
      ("message", Json.fromString(a.action)),
    )
  }
  implicit def actionEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Action] =
    jsonEncoderOf[F, Action]
}

trait MeerkatAction[F[_]]{
  def act(n: MeerkatAction.Do): F[Action]
}

object MeerkatAction {
  implicit def apply[F[_]](implicit ev: MeerkatAction[F]): MeerkatAction[F] = ev

  final case class Do(that: String) extends AnyVal

  def impl[F[_]: Applicative]: MeerkatAction[F] = new MeerkatAction[F]{
    def act(n: MeerkatAction.Do): F[Action] =
        Action("Do " + n.that + "!").pure[F]
  }
}