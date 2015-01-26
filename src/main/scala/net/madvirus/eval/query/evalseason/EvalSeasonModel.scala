package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.evalseaon.RateeType
import net.madvirus.eval.query.user.UserModel

import scala.beans.BeanProperty

import scala.collection.JavaConversions._

case class RateeMappingModel(@BeanProperty ratee: UserModel,
                             @BeanProperty `type`: RateeType,
                             @BeanProperty firstRater: UserModel,
                             @BeanProperty secondRater: UserModel,
                             colleagueRaters: Set[UserModel]
                              ) {
  def getColleagueRaters: java.util.List[UserModel] = colleagueRaters.toList.sortBy(x => x.getName())
}

case class EvalSeasonModel(
                            @BeanProperty id: String,
                            @BeanProperty name: String,
                            @BeanProperty opened: Boolean = false,
                            rateeToMappingMap: Map[String, RateeMappingModel] = Map(),
                            firstRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            secondRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            colleagueRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map())
                            ) {
  def this(id: String, name: String) =
    this(id, name, false, Map(), RaterToRateesMap(Map()), RaterToRateesMap(Map()), RaterToRateesMap(Map()))

  def getRateeMappingModels(): java.util.List[RateeMappingModel] =
    rateeToMappingMap.values.toList.sortBy(x => x.ratee.getName())


  def getRateesOfFirstRater(firstRaterId: String): Set[UserModel] =
    firstRaterToRateesMap.getRatees(firstRaterId)

  def getRateesOfSecondRater(secondRaterId: String): Set[UserModel] =
    secondRaterToRateesMap.getRatees(secondRaterId)

  def getRateesOfColleague(colleagueId: String): Set[UserModel] =
    colleagueRaterToRateesMap.getRatees(colleagueId)

  def updateMapping(mapping: RateeMappingModel): EvalSeasonModel = {
    val oldMappingOption: Option[RateeMappingModel] = rateeToMappingMap.get(mapping.ratee.getId)
    val newFirstRaterToRateeMap: RaterToRateesMap =
      firstRaterToRateesMap.updateMapping(
        mapping.ratee,
        oldMappingOption.flatMap(oldMap => Option(oldMap.getFirstRater)),
        Option(mapping.getFirstRater))

    val newSecondRaterToRateeMap: RaterToRateesMap =
      secondRaterToRateesMap.updateMapping(
        mapping.getRatee,
        oldMappingOption.flatMap(oldMap => Option(oldMap.getSecondRater)),
        Option(mapping.getSecondRater))

    val oldColleagues = oldMappingOption.flatMap(oldMap => Option(oldMap.getColleagueRaters))
    val newColleagueRateesMap = if (oldColleagues.equals(mapping.getColleagueRaters)) {
      colleagueRaterToRateesMap
    } else {
      val oldColleagueRemovedMap: RaterToRateesMap =
        if (!oldMappingOption.isEmpty) {
          oldMappingOption.get.colleagueRaters.toList.foldLeft(colleagueRaterToRateesMap) {
            (map, colleagueRater) =>
              map.removeMapping(mapping.getRatee, colleagueRater)
          }
        } else {
          colleagueRaterToRateesMap
        }

      mapping.getColleagueRaters.foldLeft(oldColleagueRemovedMap)({ (map, colleagueRater) =>
        map.addMapping(mapping.getRatee, colleagueRater)
      })
    }

    copy(
      rateeToMappingMap = this.rateeToMappingMap + (mapping.getRatee.getId -> mapping),
      firstRaterToRateesMap = newFirstRaterToRateeMap,
      secondRaterToRateesMap = newSecondRaterToRateeMap,
      colleagueRaterToRateesMap = newColleagueRateesMap
    )
  }

  def deleteMappings(rateeIds: List[String]): EvalSeasonModel = {
    var newRateeToMappings = rateeToMappingMap
    println(rateeIds)
    rateeIds.foreach(id => newRateeToMappings = newRateeToMappings - id)

    println(newRateeToMappings)
    copy(
      rateeToMappingMap = newRateeToMappings
    )
  }

  def open(): EvalSeasonModel = copy(opened = true)

  def getRateeMappingOf(rateeId: String): Option[RateeMappingModel] =
    rateeToMappingMap get rateeId
}

case class RaterToRateesMap(map: Map[String, Set[UserModel]]) {
  def getRatees(raterId: String): Set[UserModel] = map.getOrElse(raterId, Set())

  def addMapping(ratee: UserModel, rater: UserModel): RaterToRateesMap =
    RaterToRateesMap(
      map + (rater.getId -> (map.get(rater.getId).getOrElse(Set()) + ratee))
    )

  def removeMapping(ratee: UserModel, rater: UserModel): RaterToRateesMap =
    RaterToRateesMap(
      map + (rater.getId -> (map.get(rater.getId).getOrElse(Set()) - ratee))
    )

  def updateMapping(ratee: UserModel, oldRaterOpt: Option[UserModel], newRaterOpt: Option[UserModel]): RaterToRateesMap = {
    (oldRaterOpt.flatMap(um => Some(um.getId())), newRaterOpt.flatMap(um => Some(um.getId()))) match {
      case (None, None) => this

      case (Some(oldRaterId), Some(newRaterId)) if oldRaterId == newRaterId => this

      case (None, Some(raterId)) => addMapping(ratee, newRaterOpt.get)

      case (Some(oldRaterId), None) => {
        val ratees: Option[Set[UserModel]] = map.get(oldRaterId)
        if (ratees.nonEmpty)
          removeMapping(ratee, oldRaterOpt.get)
        else
          this
      }

      case (Some(oldRaterId), Some(newRaterId)) if oldRaterId != newRaterId =>
        this.removeMapping(ratee, oldRaterOpt.get).addMapping(ratee, newRaterOpt.get)
    }
  }

}
