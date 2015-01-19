package net.madvirus.eval.query.evalseason

import net.madvirus.eval.api.evalseaon.{EvalSeasonCreatedEvent, EvalSeasonEvent, EvaluationOpenedEvent, MappingUpdatedEvent}
import org.axonframework.eventhandling.annotation.EventHandler

class EvalSeasonEventListener(val repository: EvalSeasonModelRepository) {

  @EventHandler
  def handle(event: EvalSeasonEvent): Unit = event match {
    case e: EvalSeasonCreatedEvent => {
      repository.add(EvalSeasonModel(e.getEvalSeasonId, e.getName))
    }
    case e: MappingUpdatedEvent => {
      val modelOption = repository.findById(e.getEvalSeasonId)
      modelOption
        .flatMap(model => Some(model.updateMapping(e.getMapping)))
        .foreach(model => repository.add(model))
    }
    case e: EvaluationOpenedEvent => println('3')
      repository.findById(e.getId)
        .flatMap(model => Some(model.open()))
  }

}
