package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.evalseaon._
import net.madvirus.eval.query.user.{UserModel, UserModelRepository}
import org.axonframework.eventhandling.annotation.{SequenceNumber, EventHandler}

import scala.collection.JavaConversions
import scala.collection.JavaConversions._

class EvalSeasonMappingEventListener(
                                      val evalSeasonModelRepository: EvalSeasonMappingModelRepository,
                                      val userRepository: UserModelRepository) {

  @EventHandler
  def handle(event: EvalSeasonEvent, @SequenceNumber seqNumber: Long): Unit = {
    event match {
      case e: EvalSeasonCreatedEvent => {
        evalSeasonModelRepository.add(EvalSeasonMappingModel(e.getEvalSeasonId, seqNumber))
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
            evalSeason = evalSeason.updateMapping(RateeMappingModel(ratee, mapping.getType, rater1, rater2, colleagueRater), seqNumber)
          })
          evalSeasonModelRepository.add(evalSeason)
        }
      }

      case e: MappingDeletedEvent =>
        val delIds: List[String] = JavaConversions.asScalaBuffer(e.getDeletedRateeIds).toList
        evalSeasonModelRepository.findById(e.getEvalSeasonId)
          .flatMap(model => Some(model.deleteMappings(delIds, seqNumber)))
          .foreach(model => evalSeasonModelRepository.add(model))

      case _ =>
    }
  }

}
