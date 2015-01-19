package net.madvirus.eval.query.evalseason

trait EvalSeasonModelRepository {

  def add(model: EvalSeasonModel): Unit

  def findById(evalSeasonId: String): Option[EvalSeasonModel]

  def findAll(): java.util.List[EvalSeasonModel]
}
