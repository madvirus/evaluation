package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.RateeMapping

import scala.beans.BeanProperty

class EvalSeasonModelRepositoryImpl extends EvalSeasonModelRepository {

  import scala.collection.mutable.Map
  import scala.collection.JavaConversions._

  private val map: Map[String, EvalSeasonModel] = Map()

  override def add(model: EvalSeasonModel): Unit = map += (model.id -> model)

  override def findById(evalSeasonId: String): Option[EvalSeasonModel] = map.get(evalSeasonId)

  override def findAll(): java.util.List[EvalSeasonModel] = map.values.toList
}

case class EvalSeasonModel(
                            @BeanProperty id: String,
                            @BeanProperty name: String,
                            @BeanProperty opened: Boolean = false,
                            rateeToMappingMap: Map[String, RateeMapping] = Map(),
                            firstRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            secondRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            colleagueRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map())
                            ) {

  def this(id: String, name: String) =
    this(id, name, false, Map(), RaterToRateesMap(Map()), RaterToRateesMap(Map()), RaterToRateesMap(Map()))

  def getRateesOfFirstRater(firstRaterId: String): Set[String] =
    firstRaterToRateesMap.getRatees(firstRaterId)

  def getRateesOfSecondRater(secondRaterId: String): Set[String] =
    secondRaterToRateesMap.getRatees(secondRaterId)

  def getRateesOfColleague(colleagueId: String): Set[String] =
    colleagueRaterToRateesMap.getRatees(colleagueId)

  def updateMapping(mapping: RateeMapping): EvalSeasonModel = {
    val oldMappingOption = rateeToMappingMap.get(mapping.getRateeId)
    val newFirstRaterToRateeMap: RaterToRateesMap =
      firstRaterToRateesMap.updateMapping(
        mapping.getRateeId,
        oldMappingOption.flatMap(oldMap => Option(oldMap.getFirstRaterId)),
        Option(mapping.getFirstRaterId))

    val newSecondRaterToRateeMap: RaterToRateesMap =
      secondRaterToRateesMap.updateMapping(
        mapping.getRateeId,
        oldMappingOption.flatMap(oldMap => Option(oldMap.getSecondRaterId)),
        Option(mapping.getSecondRaterId))

    val oldColleagues = oldMappingOption.flatMap(oldMap => Option(oldMap.getColleagueRaterIds))
    val newColleagueRateesMap = if (oldColleagues.equals(mapping.getColleagueRaterIds)) {
      colleagueRaterToRateesMap
    } else {
      import scala.collection.JavaConversions._
      val oldColleagueRemovedMap: RaterToRateesMap =
        if (!oldMappingOption.isEmpty) {
          val oldColleagueRaterIds: List[String] =
            oldMappingOption.
              flatMap(x => Option(x.getColleagueRaterIds)).
              getOrElse(java.util.Collections.emptyList()).toList

          oldColleagueRaterIds.foldLeft(colleagueRaterToRateesMap)({
            (map, colleagueRater) =>
              map.removeMapping(mapping.getRateeId, colleagueRater)
          })
        } else {
          colleagueRaterToRateesMap
        }

      mapping.getColleagueRaterIds.foldLeft(oldColleagueRemovedMap)({ (map, colleagueRater) =>
        map.addMapping(mapping.getRateeId, colleagueRater)
      })
    }

    copy(
      rateeToMappingMap = this.rateeToMappingMap + (mapping.getRateeId -> mapping),
      firstRaterToRateesMap = newFirstRaterToRateeMap,
      secondRaterToRateesMap = newSecondRaterToRateeMap,
      colleagueRaterToRateesMap = newColleagueRateesMap
    )
  }

  def open(): Unit = copy(opened = true)

  def getRateeMappingOf(rateeId: String): Option[RateeMapping] =
    rateeToMappingMap get rateeId
}

case class RaterToRateesMap(map: Map[String, Set[String]]) {
  def getRatees(raterId: String): Set[String] = map.getOrElse(raterId, Set())

  def addMapping(rateeId: String, raterId: String): RaterToRateesMap =
    RaterToRateesMap(
      map + (raterId -> (map.get(raterId).getOrElse(Set()) + rateeId))
    )

  def removeMapping(rateeId: String, raterId: String): RaterToRateesMap =
    RaterToRateesMap(
      map + (raterId -> (map.get(raterId).getOrElse(Set()) - rateeId))
    )

  def updateMapping(rateeId: String, oldRaterIdOpt: Option[String], newRaterIdOpt: Option[String]): RaterToRateesMap = {
    (oldRaterIdOpt, newRaterIdOpt) match {
      case (None, None) => this

      case (Some(oldRaterId), Some(newRaterId)) if oldRaterId == newRaterId => this

      case (None, Some(raterId)) => addMapping(rateeId, raterId)

      case (Some(oldRaterId), None) => {
        val ratees: Option[Set[String]] = map.get(oldRaterId)
        if (ratees.nonEmpty)
          removeMapping(rateeId, oldRaterId)
        else
          this
      }

      case (Some(oldRaterId), Some(newRaterId)) if oldRaterId != newRaterId =>
        this.removeMapping(rateeId, oldRaterId).addMapping(rateeId, newRaterId)
    }
  }

}
