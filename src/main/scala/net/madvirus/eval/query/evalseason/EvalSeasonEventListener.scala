package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.evalseaon._
import net.madvirus.eval.query.user.{UserModel, UserModelRepository}
import org.axonframework.eventhandling.annotation.EventHandler

import scala.collection.JavaConversions
import scala.collection.JavaConversions._

class EvalSeasonEventListener(
                               val evalSeasonModelRepository: EvalSeasonModelRepository,
                               val userRepository: UserModelRepository) {

  @EventHandler
  def handle(event: EvalSeasonEvent): Unit = event match {
    case e: EvalSeasonCreatedEvent => {
      evalSeasonModelRepository.add(EvalSeasonModel(e.getEvalSeasonId, e.getName))
    }
    case e: MappingUpdatedEvent => {
      val modelOption = evalSeasonModelRepository.findById(e.getEvalSeasonId)
      if (modelOption.nonEmpty) {
        var evalSeason = modelOption.get

        e.getMappings.foreach(mapping => {
          val ratee = userRepository.findOne(mapping.getRateeId)
          val rater1 = if (mapping.hasFirstRater) userRepository.findOne(mapping.getFirstRaterId) else null
          val rater2 = userRepository.findOne(mapping.getSecondRaterId)
          val colleagueRater = mapping.getColleagueRaterIds.toList.foldLeft(Set[UserModel]()) { (set, colleagueId) => {
            val colUser = userRepository.findOne(colleagueId)
            if (colUser != null)
              set + colUser
            else
              set
          }
          }
          evalSeason = evalSeason.updateMapping(RateeMappingModel(ratee, mapping.getType, rater1, rater2, colleagueRater))
        })
        evalSeasonModelRepository.add(evalSeason)
      }
//      modelOption
//        .flatMap(model => {
//        val ratee = userRepository.findOne(e.getMappings.getRateeId)
//        val rater1 = if (e.getMappings.getFirstRaterId != null) userRepository.findOne(e.getMappings.getFirstRaterId) else null
//        val rater2 = userRepository.findOne(e.getMappings.getSecondRaterId)
//        val colleagueRater = e.getMappings.getColleagueRaterIds.toList.foldLeft(Set[UserModel]()) { (set, colleagueId) => {
//          val colUser = userRepository.findOne(colleagueId)
//          if (colUser != null)
//            set + colUser
//          else
//            set
//        }
//        }
//        Some(model.updateMapping(RateeMappingModel(ratee, e.getMappings.getType, rater1, rater2, colleagueRater)))
//      })
//        .foreach(model => evalSeasonModelRepository.add(model))
    }
    case e: MappingDeletedEvent =>
      val delIds: List[String] = JavaConversions.asScalaBuffer(e.getDeletedRateeIds).toList
      evalSeasonModelRepository.findById(e.getEvalSeasonId).flatMap(model => Some(model.deleteMappings(delIds)))
        .foreach(model => evalSeasonModelRepository.add(model))

    case e: EvaluationOpenedEvent =>
      evalSeasonModelRepository.findById(e.getId).flatMap(model => Some(model.open()))
        .foreach(model => evalSeasonModelRepository.add(model))
  }

}
