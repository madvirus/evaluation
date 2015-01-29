package net.madvirus.eval.query.evalseason

import java.util.Date

import net.madvirus.eval.api.evalseaon.{RateeType, EvalSeasonCreatedEvent, MappingDeletedEvent}
import net.madvirus.eval.query.user.{UserModel, UserModelRepository}
import org.hamcrest.Matchers._
import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito

class EvalSeasonMappingEventListenerTest {

  val repository: EvalSeasonMappingModelRepositoryImpl = new EvalSeasonMappingModelRepositoryImpl()
  val mockUserRepository: UserModelRepository = Mockito.mock(classOf[UserModelRepository])
  val listener = new EvalSeasonMappingEventListener(repository, mockUserRepository)

  @Test
  def whenReceiveCreateEvent_should_AddNewEvalSeasonModel_to_Repository(): Unit = {
    listener.handle(new EvalSeasonCreatedEvent("id", "name", new Date()), 1L)

    val modelOption: Option[EvalSeasonMappingModel] = repository.findById("id")
    assertThat(modelOption.nonEmpty, equalTo(true))
    val createdModel = modelOption.get
    assertThat(createdModel.id, equalTo("id"))
  }

  @Test
  def whenReceiveMappingDeletedEvent_should_DeleteMapping(): Unit = {
    val model = EvalSeasonMappingModel("id", 0L)
      .updateMapping(RateeMappingModel(
        new UserModel("ratee1", "피평가자1", ""), RateeType.MEMBER,
        new UserModel("rater1", "평가자1", ""), new UserModel("rater2", "평가자2", ""), Set[UserModel]()), 1L)
      .updateMapping(RateeMappingModel(
        new UserModel("ratee2", "피평가자2", ""), RateeType.MEMBER,
        new UserModel("rater1", "평가자1", ""), new UserModel("rater2", "평가자2", ""), Set[UserModel]()), 2L)

    repository.add(model);
    listener.handle(new MappingDeletedEvent("id", java.util.Arrays.asList("ratee1", "ratee2")), 3L)

    val seasonModel = repository.findById("id").get
    assertThat(seasonModel.getRateeMappingOf("ratee1").isEmpty, equalTo(true))
    assertThat(seasonModel.getRateeMappingOf("ratee2").isEmpty, equalTo(true))

  }
}
