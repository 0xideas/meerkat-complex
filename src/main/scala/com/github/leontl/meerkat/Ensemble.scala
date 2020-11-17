package com.github.leontl.meerkat

import epsilon.ensembles.EpsilonEnsembleThompsonSamplingLocal
import epsilon.models.GenericDummyModel

object Ensemble{
    val models = List( new GenericDummyModel[Unit, String]("a"),
        new GenericDummyModel[Unit, String]("b"))

    val ensemble = EpsilonEnsembleThompsonSamplingLocal[Int, Unit, String](
        models.zipWithIndex.map{case(k,v) => (v,k)}.toMap,
        (distr, reward) => {distr.update(reward); distr},
        (a1, a2) => if(a1 == a2) 1.0 else 0.0,
        1.0, 1.0)
    
    case class Update(val modelId:Int, val reward:Double)

}
