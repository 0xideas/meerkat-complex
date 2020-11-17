package com.github.leontl.meerkat

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import io.circe._
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.circe._
import io.circe.generic.auto._


object MeerkatRoutes {
  def routes[F[_]: Sync](H: MeerkatAction[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    import Ensemble._

    implicit val updateDecoder: EntityDecoder[F, Update] = jsonOf[F, Update]
    implicit val updateEncoder: EntityEncoder[F, Update] = jsonEncoderOf[F, Update]

    implicit val contextDecoder: EntityDecoder[F, Context] = jsonOf[F, Context]
    implicit val contextEncoder: EntityEncoder[F, Context] = jsonEncoderOf[F, Context]

    HttpRoutes.of[F] {
      case req @ POST -> Root / "action" =>
        req.decode[Context]{ context => {
            for {
              action <- H.act(MeerkatAction.Do(ensemble.act(context.context, ())))
              resp <- Ok(action)
            } yield resp
          }
        }
  
      case req @ POST -> Root / "update" => 
        req.decode[Update]{ update =>
          ensemble.update(update.modelId, update.context, update.reward)
          Ok(s"model ${update.modelId} updated with ${update.reward}!")
        }

      case req @ GET -> Root / "export" => 
        Ok(ensemble.export)
    }
  
  }
}