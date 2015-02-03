package net.madvirus.eval.query.evalseason

import org.axonframework.eventhandling.replay.ReplayingCluster
import org.axonframework.eventstore.management.Criteria

class EvanSeasonMappingModelInitializerTestImpl(val cluster: ReplayingCluster)
  extends EvanSeasonMappingModelInitializer {

  def replay(): Unit = {
    val criteria:Criteria = cluster.newCriteriaBuilder().property("type").is("EvalSeason")
    cluster.startReplay(criteria)
  }
}

