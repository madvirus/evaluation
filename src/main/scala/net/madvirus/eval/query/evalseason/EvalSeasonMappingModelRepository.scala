package net.madvirus.eval.query.evalseason

trait EvalSeasonMappingModelRepository {

  def add(model: EvalSeasonMappingModel): Unit

  def findById(evalSeasonId: String): Option[EvalSeasonMappingModel]

  def findAll(): java.util.List[EvalSeasonMappingModel]
}

class EvalSeasonMappingModelRepositoryImpl extends EvalSeasonMappingModelRepository {

  import scala.collection.JavaConversions._
  import scala.collection.mutable.Map

  private val map: Map[String, EvalSeasonMappingModel] = Map()

  override def add(model: EvalSeasonMappingModel): Unit = map += (model.id -> model)

  override def findById(evalSeasonId: String): Option[EvalSeasonMappingModel] = map.get(evalSeasonId)

  override def findAll(): java.util.List[EvalSeasonMappingModel] = map.values.toList
}

