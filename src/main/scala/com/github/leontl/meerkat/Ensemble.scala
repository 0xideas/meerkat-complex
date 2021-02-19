package com.github.leontl.meerkat

import ada.ensembles.ContextualThompsonSampling
import ada.models.GenericStaticModelContext
import ada.components.distributions.BayesianSampleRegressionDistribution
import ada.`package`.Reward

import io.circe.Json
import scala.collection.mutable.{Map => MutableMap}
import breeze.linalg._

object Ensemble{
    val models = List( new GenericStaticModelContext[Int, Array[Double], Unit, String, BayesianSampleRegressionDistribution]("a")(s => Json.fromString(s)),
        new GenericStaticModelContext[Int, Array[Double], Unit, String, BayesianSampleRegressionDistribution]("b")(s => Json.fromString(s)))

    val modelsMap = models.zipWithIndex.map{case(k,v) => (v,k)}.toMap
    val ensemble:ContextualThompsonSampling[Int, Unit, String] = new ContextualThompsonSampling[Int, Unit, String](
        (i) => modelsMap(i),
        () => modelsMap.keys.toList,
        Map(0 -> new BayesianSampleRegressionDistribution(2, 0.3, 1.0),
            1 -> new BayesianSampleRegressionDistribution(2, 0.3, 1.0)))
    
    case class Update(val modelId: Int, val context: Array[Double], val reward:Reward)

    case class Context(val context: Array[Double])

    case class ChangeBeta(val modelId: Int, val increment: Double, val factor: Double, val max: Double)

    case class Parameters(val modelId: Int, val mean: Array[Double], val covInv: Array[Double])

}

