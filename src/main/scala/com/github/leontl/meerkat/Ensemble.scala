package com.github.leontl.meerkat

import ada.core.ensembles.ThompsonSamplingLocalWithContext
import ada.core.models.GenericStaticModel
import ada.core.components.distributions.BayesianSampleRegressionContext

import io.circe.Json
import scala.collection.mutable.{Map => MutableMap}

object Ensemble{
    val models = List( new GenericStaticModel[Unit, String]("a")(s => Json.fromString(s)),
        new GenericStaticModel[Unit, String]("b")(s => Json.fromString(s)))

    val ensemble = new ThompsonSamplingLocalWithContext[Int, Array[Double], Unit, String, BayesianSampleRegressionContext](
        models.zipWithIndex.map{case(k,v) => (v,k)}.toMap,
        MutableMap(0 -> new BayesianSampleRegressionContext(5, 0.3, 1.0),
                   1 -> new BayesianSampleRegressionContext(5, 0.3, 1.0)),
        (a1, a2) => if(a1 == a2) 1.0 else 0.0)
    
    case class Update(val modelId: Int, val context: Array[Double], val reward:Double)

    case class Context(val context: Array[Double])

}

