package com.github.leontl.meerkat

import ada.core.ensembles.ThompsonSamplingWithContext
import ada.core.models.GenericStaticModel
import ada.core.components.distributions.BayesianSampleRegressionContext

import io.circe.Json
import scala.collection.mutable.{Map => MutableMap}

object Ensemble{
    val models = List( new GenericStaticModel[Int, Unit, String]("a")(s => Json.fromString(s)),
        new GenericStaticModel[Int, Unit, String]("b")(s => Json.fromString(s)))

    val ensemble = new ThompsonSamplingWithContext[Int, Array[Double], Unit, String, BayesianSampleRegressionContext](
        models.zipWithIndex.map{case(k,v) => (v,k)}.toMap,
        Map(0 -> new BayesianSampleRegressionContext(500, 0.3, 1.0),
            1 -> new BayesianSampleRegressionContext(500, 0.3, 1.0)))
    
    case class Update(val modelId: Int, val context: Array[Double], val reward:Double)

    case class Context(val context: Array[Double])

    case class ChangeBeta(val modelId: Int, val increment: Double, val factor: Double, val max: Double)

}

