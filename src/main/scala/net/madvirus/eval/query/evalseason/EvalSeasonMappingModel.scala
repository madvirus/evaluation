package net.madvirus.eval.query.evalseason

import net.madvirus.eval.domain.evalseason.RateeType
import net.madvirus.eval.query.user.UserModel

import scala.beans.BeanProperty

import scala.collection.JavaConversions._

case class RateeMappingModel(@BeanProperty ratee: UserModel,
                             @BeanProperty `type`: RateeType,
                             @BeanProperty firstRater: UserModel,
                             @BeanProperty secondRater: UserModel,
                             colleagueRaters: Set[UserModel]
                              ) {
  def this(ratee: UserModel, `type`: RateeType, firstRater: UserModel, secondRater: UserModel) =
    this(ratee, `type`, firstRater, secondRater, Set())

  def getColleagueRaters: java.util.List[UserModel] = colleagueRaters.toList.sortBy(x => x.getName())

  def containsColleagueRater(raterId: String): Boolean = {
    val userModelOpt = colleagueRaters.find(userModel => userModel.getId() == raterId)
    return userModelOpt.nonEmpty
  }

  def hasFirstRater(): Boolean = {
    return firstRater != null
  }
}

case class EvalSeasonMappingModel(
                            @BeanProperty id: String,
                            @BeanProperty sequenceNumber: Long = 0,
                            rateeToMappingMap: Map[String, RateeMappingModel] = Map(),
                            firstRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            secondRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map()),
                            colleagueRaterToRateesMap: RaterToRateesMap = RaterToRateesMap(Map())
                            ) {
  def this(id: String, seqNumber: Long) =
    this(id, seqNumber, Map(), RaterToRateesMap(Map()), RaterToRateesMap(Map()), RaterToRateesMap(Map()))

  def getRateeMappingModels(): java.util.List[RateeMappingModel] =
    rateeToMappingMap.values.toList.sortBy(x => x.ratee.getName())

  def getRateesOfFirstRater(firstRaterId: String): java.util.Set[UserModel] =
    firstRaterToRateesMap.getRatees(firstRaterId)

  def getRateesOfSecondRater(secondRaterId: String): java.util.Set[UserModel] =
    secondRaterToRateesMap.getRatees(secondRaterId)

  def getRateesOfColleague(colleagueId: String): java.util.Set[UserModel] =
    colleagueRaterToRateesMap.getRatees(colleagueId)

  def updateMapping(mapping: RateeMappingModel, seqNumber: Long): EvalSeasonMappingModel = {
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
      sequenceNumber = seqNumber,
      rateeToMappingMap = this.rateeToMappingMap + (mapping.getRatee.getId -> mapping),
      firstRaterToRateesMap = newFirstRaterToRateeMap,
      secondRaterToRateesMap = newSecondRaterToRateeMap,
      colleagueRaterToRateesMap = newColleagueRateesMap
    )
  }

  def deleteMappings(rateeIds: List[String], seqNumber: Long): EvalSeasonMappingModel = {
    var newRateeToMappings = rateeToMappingMap
    var newFirstRaterToRateesMap = firstRaterToRateesMap
    var newSecondRaterToRateesMap = secondRaterToRateesMap
    var newColleagueRaterToRateesMap = colleagueRaterToRateesMap
    rateeIds.foreach(rateeId => {
      val mappingOpt = rateeToMappingMap.get(rateeId)
      if (mappingOpt.nonEmpty) {
        val mapping = mappingOpt.get
        newRateeToMappings = newRateeToMappings - rateeId
        if (mapping.hasFirstRater()) {
          newFirstRaterToRateesMap = newFirstRaterToRateesMap.removeMapping(mapping.ratee, mapping.firstRater)
        }
        newSecondRaterToRateesMap = newSecondRaterToRateesMap.removeMapping(mapping.ratee, mapping.secondRater)
        mapping.colleagueRaters.foreach(um => {
          newColleagueRaterToRateesMap = newColleagueRaterToRateesMap.removeMapping(mapping.ratee, um)
        })
      }
    })

    copy(
      sequenceNumber = seqNumber,
      rateeToMappingMap = newRateeToMappings,
      firstRaterToRateesMap = newFirstRaterToRateesMap,
      secondRaterToRateesMap = newSecondRaterToRateesMap,
      colleagueRaterToRateesMap = newColleagueRaterToRateesMap
    )
  }

  def getRateeMappingOf(rateeId: String): Option[RateeMappingModel] = rateeToMappingMap get rateeId
  def containsRatee(rateeId: String): Boolean = rateeToMappingMap.contains(rateeId)
  def containsFirstRater(raterId: String): Boolean = firstRaterToRateesMap.containsRater(raterId)
  def containsSecondRater(raterId: String): Boolean = secondRaterToRateesMap.containsRater(raterId)
  def containsColleaguRater(raterId: String): Boolean = colleagueRaterToRateesMap.containsRater(raterId)
}

case class RaterToRateesMap(map: Map[String, Set[UserModel]]) {
  def containsRater(raterId: String): Boolean = map.contains(raterId)
  def getRatees(raterId: String): Set[UserModel] = map.getOrElse(raterId, Set())

  def addMapping(ratee: UserModel, rater: UserModel): RaterToRateesMap =
    RaterToRateesMap(
      map + (rater.getId -> (map.get(rater.getId).getOrElse(Set()) + ratee))
    )

  def removeMapping(ratee: UserModel, rater: UserModel): RaterToRateesMap = {
    val newSet = map.get(rater.getId).getOrElse(Set()) - ratee
    if (newSet.isEmpty) {
      RaterToRateesMap(
        map - rater.getId
      )
    } else {
      RaterToRateesMap(
        map + (rater.getId -> newSet)
      )
    }
  }

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
