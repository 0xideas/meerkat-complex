package ada.ensembles

import breeze.stats.mode
import io.circe.Json

import ada._
import ada.interface._
import ada.components.selectors._
import ada.components.distributions._


class GreedySoftmaxEnsemble[ModelID, ModelData, ModelAction, AggregateReward <: SimpleDistribution]
    (models: ModelID  => StackableModel[ModelID, ModelData, ModelAction],
     modelKeys: () => List[ModelID],
    modelRewards: Map[ModelID, AggregateReward],
    epsilon: Double)
    extends GreedyEnsembleAbstract[ModelID, ModelData, ModelAction, AggregateReward](models, modelKeys, modelRewards, epsilon)
    with GreedySoftmax[ModelID, ModelData, ModelAction]

