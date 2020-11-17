package ada.core.interface

import scala.collection.mutable.{Map => MutableMap}

import ada._
import ada.core.components.distributions.ContextualDistribution


abstract class AdaEnsemble[ModelID, ModelData, ModelAction, AggregateReward]
    (models: Map[ModelID, Model[ModelData, ModelAction]],
    modelRewards: MutableMap[ModelID, AggregateReward])
    extends Model[ModelData, ModelAction]{
    def evaluate(action: ModelAction, optimalAction: ModelAction): Reward

    def models(): Map[ModelID, Model[ModelData, ModelAction]] = models
    def modelRewards(): MutableMap[ModelID, AggregateReward] = modelRewards
    def modelRewards(id: ModelID):  AggregateReward = modelRewards()(id)

}

abstract class SimpleEnsemble[ModelID, ModelData, ModelAction, AggregateReward]
    (models: Map[ModelID, Model[ModelData, ModelAction]],
    modelRewards: MutableMap[ModelID, AggregateReward])
    extends AdaEnsemble[ModelID,  ModelData, ModelAction, AggregateReward](models, modelRewards)
    with SimpleModel[ModelData, ModelAction]{
    def actWithID(data: ModelData): (ModelAction, ModelID)
    def act(data: ModelData): ModelAction = actWithID(data)._1
    def update(modelId: ModelID, reward: Reward): Unit
    def update(modelId: ModelID, action: ModelAction, optimalAction: ModelAction): Unit = 
        update(modelId, evaluate(action, optimalAction))

}

abstract class ContextualEnsemble[ModelID, Context, ModelData, ModelAction, AggregateReward]
    (models: Map[ModelID, Model[ModelData, ModelAction]],
    modelRewards: MutableMap[ModelID, AggregateReward])
    extends AdaEnsemble[ModelID,  ModelData, ModelAction, AggregateReward](models, modelRewards)
    with ContextualModel[Context, ModelData, ModelAction]{
    def update(modelId: ModelID, context: Context, reward: Reward): Unit
    def update(modelId: ModelID, context: Context, action: ModelAction, optimalAction: ModelAction): Unit = 
        update(modelId, context, evaluate(action, optimalAction))
    def actWithID(context: Context, data: ModelData): (ModelAction, ModelID)
    def act(context: Context, data: ModelData): ModelAction = actWithID(context, data)._1
    //override def act[Context](context: Context, data: ModelData): ModelAction = actWithID(context, data)._1
}