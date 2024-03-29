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
import ada.`package`.Reward

object MeerkatRoutes {
  def routes[F[_]: Sync](H: MeerkatAction[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    import Ensemble._

    //implicit val rewardDeconder: EntityDecoder[F, Reward] = jsonOf[F, Reward2]
    //implicit val rewardEnconder: EntityEncoder[F, Reward] = jsonEncoderOf[F, Reward2]

    implicit val updateDecoder: EntityDecoder[F, Update] = jsonOf[F, Update]
    implicit val updateEncoder: EntityEncoder[F, Update] = jsonEncoderOf[F, Update]

    implicit val contextDecoder: EntityDecoder[F, Context] = jsonOf[F, Context]
    implicit val contextEncoder: EntityEncoder[F, Context] = jsonEncoderOf[F, Context]

    implicit val changeBetaDecoder: EntityDecoder[F, ChangeBeta] = jsonOf[F, ChangeBeta]
    implicit val changeBetaEncoder: EntityEncoder[F, ChangeBeta] = jsonEncoderOf[F, ChangeBeta]

    implicit val parametersDecoder: EntityDecoder[F, Parameters] = jsonOf[F, Parameters]
    implicit val parametersEncoder: EntityEncoder[F, Parameters] = jsonEncoderOf[F, Parameters]

    HttpRoutes.of[F] {
      case req @ POST -> Root / "action" =>
        req.decode[Context]{ context => {
            for {
              action <- H.act(MeerkatAction.Do(ensemble.act(List(), context.context, ())))
              resp <- Ok(action)
            } yield resp
          }
        }

      case req @ POST -> Root / "update" => 
        req.decode[Update]{ update =>
          ensemble.update(List(update.modelId), update.context, (), new Reward(update.reward))

          Ok(s"contextual model ${update.modelId} updated with ${update.reward}!")
        }

      case req @ POST -> Root / "set-beta" => 
        req.decode[ChangeBeta]{ betaParameters =>
          ensemble.modelRewards(betaParameters.modelId).setBeta(betaParameters.increment, betaParameters.factor, betaParameters.max)
          Ok(s"model ${betaParameters.modelId} beta changed to ${ensemble.modelRewards(betaParameters.modelId).beta}!")
        }

      case req @ POST -> Root / "set-parameters" => 
        req.decode[Parameters]{ parameters =>
          ensemble.modelRewards(parameters.modelId).setParameters(parameters.mean, parameters.covInv)
          Ok("Paramters set.")
        }

      case req @ GET -> Root / "export" => 
        Ok(ensemble.export)
    }
  
  }
}