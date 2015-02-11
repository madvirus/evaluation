package net.madvirus.eval.query.evalseason

import org.axonframework.eventhandling.replay.ReplayingCluster
import org.axonframework.eventstore.management.Criteria

class EvanSeasonMappingModelInitializerTestImpl(val cluster: ReplayingCluster,
                                                 val evalSeasonMappingModelRepository: EvalSeasonMappingModelRepository)
  extends EvanSeasonMappingModelInitializer {

  def replay(): Unit = {
    evalSeasonMappingModelRepository.clear()
    val criteria:Criteria = cluster.newCriteriaBuilder().property("type").is("EvalSeason")
    cluster.startReplay(criteria)
  }
}

