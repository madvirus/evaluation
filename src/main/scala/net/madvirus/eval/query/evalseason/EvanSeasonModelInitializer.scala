package net.madvirus.eval.query.evalseason

import org.axonframework.eventhandling.replay.ReplayingCluster
import org.axonframework.eventstore.management.Criteria
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

class EvanSeasonModelInitializer(val cluster: ReplayingCluster) extends ApplicationListener[ContextRefreshedEvent] {

  override def onApplicationEvent(event: ContextRefreshedEvent): Unit = {
    val criteria:Criteria = cluster.newCriteriaBuilder().property("type").is("EvalSeason")
    cluster.startReplay(criteria)
  }
}
