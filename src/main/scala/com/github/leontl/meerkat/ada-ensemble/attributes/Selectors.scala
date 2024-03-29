package ada.components.selectors

import scala.collection.mutable.{Map => MutableMap}

import ada._
import ada.interface._
import ada.components.distributions._


trait Selector[ModelID, ModelData, ModelAction]{
    protected val rnd = new scala.util.Random(101)

    def _sortModel[AggregateReward <: SimpleDistribution](
                 modelKeys: () => List[ModelID],
                 modelRewards: ModelID => AggregateReward): List[(ModelID, Reward)] = {
        val modelIds = modelKeys()
        val modelsSorted = modelIds.map(modelId => (modelId, modelRewards(modelId).draw))
                                        .toList
                                        .sortWith(_._2 > _._2)
                                        .map{case(id, value) => (id, new Reward(value))}
        modelsSorted
    }

    def _sortModel[Context, AggregateReward <: ConditionalDistribution[Context]]
    			  (modelKeys: () => List[ModelID],
                   modelRewards: ModelID => AggregateReward,
                   context: Context): List[(ModelID, Reward)] = {
        val modelIds = modelKeys()
        val modelsSorted = modelIds.map(modelId => (modelId, modelRewards(modelId).draw(context)))
                                        .toList
                                        .sortWith(_._2 > _._2)
                                        .map{case(id, value) => (id, new Reward(value))}

        modelsSorted
    }

    def _selectModel( aggregateRewardsDouble: List[(ModelID, Reward)]): ModelID
    
}



trait SoftmaxSelector[ModelID, ModelData, ModelAction]
    extends Selector[ModelID, ModelData, ModelAction]{

    def _selectModel(aggregateRewardsDouble: List[(ModelID, Reward)]):  ModelID = {
        val totalReward: Reward = new Reward(aggregateRewardsDouble.foldLeft(0.0)((agg, tup) => agg + tup._2.value))
        require(totalReward.value > 0.0, "the rewards of the available models cannot sum to less than 0")
        val cumulativeProb: List[(Probability, Probability)] = 
        	aggregateRewardsDouble
        		.scanLeft((new Probability(0.0), new Probability(0.0)))((acc, item) => (acc._2, new Probability(acc._2.value + item._2.value/totalReward.value))).tail

        val modelsCumulativeProb: List[(ModelID, (Probability, Probability))] = 
        	aggregateRewardsDouble.map(_._1).zip(cumulativeProb)

        val selector = rnd.nextDouble()
        val selectedModelId: ModelID = 
        	modelsCumulativeProb.filter{case(model, bounds) => 
        								(selector >= bounds._1.value) && (selector <= bounds._2.value)}(0)._1


        selectedModelId
    }
}

trait RandomSelector[ModelID, ModelData, ModelAction]
    extends Selector[ModelID, ModelData, ModelAction]{

    def _selectModel(aggregateRewardsDouble: List[(ModelID, Reward)]): ModelID = {
        val modelKeysV = aggregateRewardsDouble.map(_._1)
        val selector = rnd.nextInt(modelKeysV.length)
        val selectedModelId = modelKeysV(selector)
        selectedModelId
    }
}

