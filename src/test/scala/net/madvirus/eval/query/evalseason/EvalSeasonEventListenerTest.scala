package net.madvirus.eval.query.evalseason

import java.util.Date

import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test

class EvalSeasonEventListenerTest {

  @Test
  def whenReceiveCreateEvent_addNewEvalSeasonModel_to_Repository(): Unit = {
    val repository: EvalSeasonModelRepositoryImpl = new EvalSeasonModelRepositoryImpl()
    val listener = new EvalSeasonEventListener(repository)
    listener.handle(new EvalSeasonCreatedEvent("id", "name", new Date()))

    val modelOption:Option[EvalSeasonModel] = repository.findById("id")
    assertThat(modelOption.nonEmpty, equalTo(true))
    val createdModel = modelOption.get
    assertThat(createdModel.id, equalTo("id"))
    assertThat(createdModel.name, equalTo("name"))
  }
}
