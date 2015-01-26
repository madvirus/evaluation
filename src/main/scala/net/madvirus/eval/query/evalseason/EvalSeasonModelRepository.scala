package net.madvirus.eval.query.evalseason

trait EvalSeasonModelRepository {

  def add(model: EvalSeasonModel): Unit

  def findById(evalSeasonId: String): Option[EvalSeasonModel]

  def findAll(): java.util.List[EvalSeasonModel]
}

class EvalSeasonModelRepositoryImpl extends EvalSeasonModelRepository {

  import scala.collection.JavaConversions._
  import scala.collection.mutable.Map

  private val map: Map[String, EvalSeasonModel] = Map()

  override def add(model: EvalSeasonModel): Unit = map += (model.id -> model)

  override def findById(evalSeasonId: String): Option[EvalSeasonModel] = map.get(evalSeasonId)

  override def findAll(): java.util.List[EvalSeasonModel] = map.values.toList
}

