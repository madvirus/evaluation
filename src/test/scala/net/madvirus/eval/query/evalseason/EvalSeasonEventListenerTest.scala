package net.madvirus.eval.query.evalseason

import java.util.Date

import net.madvirus.eval.api.evalseaon.{RateeType, EvalSeasonCreatedEvent, MappingDeletedEvent}
import net.madvirus.eval.query.user.{UserModel, UserModelRepository}
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito

class EvalSeasonEventListenerTest {

  val repository: EvalSeasonModelRepositoryImpl = new EvalSeasonModelRepositoryImpl()
  val mockUserRepository: UserModelRepository = Mockito.mock(classOf[UserModelRepository])
  val listener = new EvalSeasonEventListener(repository, mockUserRepository)

  @Test
  def whenReceiveCreateEvent_should_AddNewEvalSeasonModel_to_Repository(): Unit = {
    listener.handle(new EvalSeasonCreatedEvent("id", "name", new Date()))

    val modelOption: Option[EvalSeasonModel] = repository.findById("id")
    assertThat(modelOption.nonEmpty, equalTo(true))
    val createdModel = modelOption.get
    assertThat(createdModel.id, equalTo("id"))
    assertThat(createdModel.name, equalTo("name"))
  }

  @Test
  def whenReceiveMappingDeletedEvent_should_DeleteMapping(): Unit = {
    val model = EvalSeasonModel("id", "name")
      .updateMapping(RateeMappingModel(
        new UserModel("ratee1", "피평가자1", ""), RateeType.MEMBER,
        new UserModel("rater1", "평가자1", ""), new UserModel("rater2", "평가자2", ""), Set[UserModel]()))
      .updateMapping(RateeMappingModel(
        new UserModel("ratee2", "피평가자2", ""), RateeType.MEMBER,
        new UserModel("rater1", "평가자1", ""), new UserModel("rater2", "평가자2", ""), Set[UserModel]()))

    repository.add(model);
    listener.handle(new MappingDeletedEvent("id", java.util.Arrays.asList("ratee1", "ratee2")))

    val seasonModel = repository.findById("id").get
    assertThat(seasonModel.getRateeMappingOf("ratee1").isEmpty, equalTo(true))
    assertThat(seasonModel.getRateeMappingOf("ratee2").isEmpty, equalTo(true))

  }
}
