package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.testhelper.CreationHelper;
import net.madvirus.eval.web.dataloader.EvalSeasonData;
import net.madvirus.eval.web.dataloader.EvalSeasonDataLoader;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.repository.Repository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static net.madvirus.eval.command.personaleval.NestedContext.nestedRun;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateSelfPerformanceEvalCommandTest {
    protected FixtureConfiguration fixture;
    private Repository<PersonalEval> repository;
    private EvalSeasonDataLoader mockEvalSeasonDataLoader = mock(EvalSeasonDataLoader.class);

    @Before
    public void setUp() throws Exception {
        initFixture();
    }

    private void initFixture() {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        repository = fixture.getRepository();
        fixture.registerAnnotatedCommandHandler(new CreatePersonalEvalCommandHandler(repository));
        fixture.registerAnnotatedCommandHandler(new UpdateSelfEvalCommandHandler(repository, mockEvalSeasonDataLoader));
        fixture.registerAnnotatedCommandHandler(new AggregateAnnotationCommandHandler(PersonalEval.class, repository));
    }

    @Test
    public void givenNoEvalSeason_thenThrow_ex() throws Exception {
        when(mockEvalSeasonDataLoader.load(any())).thenReturn(Optional.<EvalSeasonData>empty());
        fixture.given()
                .when(updateSelfPerfEvalCmdWithDraft())
                .expectException(EvalSeasonNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_but_notRatee_thenThrow_ex() throws Exception {
        givenEvalSeasonData("EVAL1024");

        fixture.given()
                .when(updateSelfPerfEvalCmdWithDraft())
                .expectException(RateeNotFoundException.class);
    }

    private void givenEvalSeasonData(String evalSeasonId, String... rateeIds) {
        EvalSeasonData mockEvalSeasonData = mock(EvalSeasonData.class);
        EvalSeasonMappingModel mockMappingModel = mock(EvalSeasonMappingModel.class);
        when(mockEvalSeasonData.getMappingModel()).thenReturn(mockMappingModel);
        for (String rateeId : rateeIds)
            when(mockMappingModel.containsRatee(rateeId)).thenReturn(true);
        when(mockEvalSeasonDataLoader.load(evalSeasonId)).thenReturn(Optional.of(mockEvalSeasonData));
    }

    @Test
    public void givenEvalSeason_and_ContainingRatee_and_PersnalEvalNotFound() throws Exception {
        givenEvalSeasonData("EVAL1024", "bkchoi");

        EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

        fixture.given()
                .when(updateSelfPerfEvalCmdWithDraft())
                .expectEventsMatching(captureMatcher);

        List<Object> events = captureMatcher.getPayloads();
        PersonalEvaluationCreatedEvent createdEvent = (PersonalEvaluationCreatedEvent) events.get(0);
        SelfPerformanceEvaluatedEvent updatedEvent = (SelfPerformanceEvaluatedEvent) events.get(1);
    }

    @Test
    public void givenEvalSeason_and_ContainingRatee_and_PersnalEvalExist() throws Exception {
        givenEvalSeasonData("EVAL1024", "bkchoi");

        nestedRun("임시 보관 상태 업데이트면 업데이트 되어야 함", () -> {
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            fixture.given(createEvent())
                    .when(updateSelfPerfEvalCmdWithDraft())
                    .expectEventsMatching(captureMatcher);

            SelfPerformanceEvaluatedEvent selfPerfEvalEvent = (SelfPerformanceEvaluatedEvent) captureMatcher.getPayload();
            assertThat(selfPerfEvalEvent.getPersonalEvalId(), equalTo("EVAL1024-bkchoi"));
            assertThat(selfPerfEvalEvent.isDone(), equalTo(false));
            List<PerformanceItemAndSelfEval> evals = selfPerfEvalEvent.getPerformanceItemAndSelfEval();
            assertThat(evals, hasSize(2));

            runInUOW(() -> {
                PersonalEval personalEval = repository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfPerfEvalDone(), equalTo(false));
            });
        });

        nestedRun("완료 상태 업데이트 요청인데 가중치 합이 100이 아니면, 익셉션 발생함", () -> {
            initFixture();
            fixture.given(createEvent())
                    .when(updateSelfPerfEvalCmdWithNot100Done())
                    .expectException(InvalidWeightSumException.class);
        });

        nestedRun("가중치 합이 100인 완료 상태 업데이트 요청이면, 업데이트 됨", () -> {
            initFixture();
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            fixture.given(createEvent())
                    .when(updateSelfPerfEvalCmdWith100Done())
                    .expectEventsMatching(captureMatcher);

            runInUOW(() -> {
                PersonalEval personalEval = repository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfPerfEvalDone(), equalTo(true));
            });
        });
    }

    private UpdateSelfPerformanceEvalCommand updateSelfPerfEvalCmdWithNot100Done() {
        return createUpdateCommand(true);
    }

    private UpdateSelfPerformanceEvalCommand updateSelfPerfEvalCmdWithDraft() {
        return createUpdateCommand(false);
    }

    private UpdateSelfPerformanceEvalCommand updateSelfPerfEvalCmdWith100Done() {
        return createUpdateCommand(true, 50, 50);
    }

    private UpdateSelfPerformanceEvalCommand createUpdateCommand(boolean done, int... weights) {
        return CreationHelper.updateSelfPerfEvalCommand("EVAL1024-bkchoi", "EVAL1024", "bkchoi", done, weights);
    }

    private UpdateSelfPerformanceEvalCommand createUpdateCommand(boolean done) {
        return createUpdateCommand(done, 10, 20);
    }

    private PersonalEvaluationCreatedEvent createEvent() {
        return new PersonalEvaluationCreatedEvent("EVAL1024-bkchoi", "EVAL1024", "bkchoi");
    }

}
