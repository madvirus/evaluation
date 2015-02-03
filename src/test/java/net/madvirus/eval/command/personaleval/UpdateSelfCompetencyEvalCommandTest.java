package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.UpdateSelfCompetencyEvalCommand;
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
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateSelfCompetencyEvalCommandTest {
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
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectException(EvalSeasonNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_but_notRatee_thenThrow_ex() throws Exception {
        givenEvalSeasonData("EVAL1024");

        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
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
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectEventsMatching(captureMatcher);

        List<Object> events = captureMatcher.getPayloads();
        PersonalEvaluationCreatedEvent createdEvent = (PersonalEvaluationCreatedEvent) events.get(0);
        SelfCompetencyEvaluatedEvent updatedEvent = (SelfCompetencyEvaluatedEvent) events.get(1);
    }

    @Test
    public void givenEvalSeason_and_ContainingRatee_and_PersnalEvalExist() throws Exception {
        givenEvalSeasonData("EVAL1024", "bkchoi");

        NestedContext.nestedRun("임시 보관 상태 업데이트면 업데이트 되어야 함", () -> {
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            fixture.given(createEvent())
                    .when(updateSelfCompeEvalCmdWithDraft())
                    .expectEventsMatching(captureMatcher);

            SelfCompetencyEvaluatedEvent selfCompeEvalEvent = (SelfCompetencyEvaluatedEvent) captureMatcher.getPayload();
            assertThat(selfCompeEvalEvent.getPersonalEvalId(), equalTo("EVAL1024-bkchoi"));
            assertThat(selfCompeEvalEvent.isDone(), equalTo(false));

            runInUOW(() -> {
                PersonalEval personalEval = repository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(false));
            });
        });

        NestedContext.nestedRun("완료 상태 업데이트 요청이면, 업데이트 됨", () -> {
            initFixture();
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            fixture.given(createEvent())
                    .when(updateSelfCompeEvalCmdWithDone())
                    .expectEventsMatching(captureMatcher);

            runInUOW(() -> {
                PersonalEval personalEval = repository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(true));
            });
        });
    }

    private UpdateSelfCompetencyEvalCommand updateSelfPerfEvalCmdWithNot100Done() {
        return createUpdateCommand(true);
    }

    private UpdateSelfCompetencyEvalCommand updateSelfCompeEvalCmdWithDraft() {
        return createUpdateCommand(false);
    }

    private UpdateSelfCompetencyEvalCommand updateSelfCompeEvalCmdWithDone() {
        return createUpdateCommand(true);
    }

    private UpdateSelfCompetencyEvalCommand createUpdateCommand(boolean done) {
        return createUpdateCommand(done, true, false);
    }

    private UpdateSelfCompetencyEvalCommand createUpdateCommand(boolean done, boolean hasLeadership, boolean hasAm) {
        return CreationHelper.updateSelfCompeEvalCommand("EVAL1024-bkchoi", "EVAL1024", "bkchoi", done, hasLeadership, hasAm);
    }

    private PersonalEvaluationCreatedEvent createEvent() {
        return new PersonalEvaluationCreatedEvent("EVAL1024-bkchoi", "EVAL1024", "bkchoi");
    }

}
