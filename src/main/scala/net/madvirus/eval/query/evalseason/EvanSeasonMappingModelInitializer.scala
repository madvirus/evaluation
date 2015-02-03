package net.madvirus.eval.query.evalseason

import org.axonframework.eventhandling.replay.ReplayingCluster
import org.axonframework.eventstore.management.Criteria
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

trait EvanSeasonMappingModelInitializer {
  def replay()
}

class EvanSeasonMappingModelInitializerImpl(val cluster: ReplayingCluster)
  extends ApplicationListener[ContextRefreshedEvent] with EvanSeasonMappingModelInitializer {

  override def onApplicationEvent(event: ContextRefreshedEvent): Unit = {
    replay()
  }

  def replay(): Unit = {
    val criteria:Criteria = cluster.newCriteriaBuilder().property("type").is("EvalSeason")
    cluster.startReplay(criteria)
  }
}
