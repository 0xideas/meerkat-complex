package ada.assemblies

import breeze.stats.mode
import io.circe.Json

import ada._
import ada.interface._
import ada.components.selectors._
import ada.components.distributions._



class StackableAssembly1[ModelID, ModelData, ModelAction, AggregateReward <: ExportUpdateable](
    ensembles: List[StackableEnsemble1[ModelID, ModelData, ModelAction, AggregateReward]]){
    def actWithID(data: ModelData, selectedIds: List[ModelID]):  (List[ModelAction], List[List[ModelID]]) = {
        ensembles.map(ensemble => ensemble.actWithID(data, selectedIds))
                    .unzip
    }
    def update(modelIds: List[List[ModelID]], data: ModelData, reward: Reward): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, data, reward)}
    }
    def update(modelIds: List[List[ModelID]], data: ModelData, action: ModelAction): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, data, action)}
    }
}

class StackableAssembly2[ModelID, ModelData, ModelAction, AggregateReward <: ExportUpdateableContext[ModelData]](
    ensembles: List[StackableEnsemble2[ModelID, ModelData, ModelAction, AggregateReward]]){
        def actWithID(data: ModelData, selectedIds: List[ModelID]):  (List[ModelAction], List[List[ModelID]]) = {
            ensembles.map(ensemble => ensemble.actWithID(data, selectedIds))
                     .unzip
        }
    def update(modelIds: List[List[ModelID]], data: ModelData, reward: Reward): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, data, reward)}
    }
    def update(modelIds: List[List[ModelID]], data: ModelData, action: ModelAction): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, data, action)}
    }
}

class ContextualAssembly[ModelID, Context, ModelData, ModelAction, AggregateReward <: ExportUpdateableContext[Context]](
    ensembles: List[ContextualEnsemble[ModelID, Context, ModelData, ModelAction, AggregateReward]]){
        def actWithID(context: Context, data: ModelData, selectedIds: List[ModelID]):  (List[ModelAction], List[List[ModelID]]) = {
            ensembles.map(ensemble => ensemble.actWithID(context, data, selectedIds))
                     .unzip
        }
    def update(modelIds: List[List[ModelID]], context: Context, data: ModelData, reward: Reward): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, context, data, reward)}
    }
    def update(modelIds: List[List[ModelID]], context: Context, data: ModelData, action: ModelAction): Unit = {
        ensembles.zip(modelIds).map{case(ensemble, modelIds_)  => ensemble.update(modelIds_, context, data, action)}
    }
}