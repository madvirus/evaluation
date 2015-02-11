package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.testhelper.CreationHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateSelfCompetencyEvalCommandTest {
    protected FixtureConfiguration fixture;
    private Repository<PersonalEval> personalEvalRepository;
    private Repository<EvalSeason> mockEvalSeasonRepository;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        personalEvalRepository = fixture.getRepository();
        mockEvalSeasonRepository = mock(Repository.class);
        fixture.registerAnnotatedCommandHandler(new UpdateSelfEvalCommandHandler(personalEvalRepository, mockEvalSeasonRepository));
    }

    @Test
    public void givenNoEvalSeason_thenThrow_ex() throws Exception {
        givenNoEvalSeason();
        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectException(EvalSeasonNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_but_NoRatee_thenThrow_ex() throws Exception {
        givenEvalSeasonData("EVAL1024");

        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectException(RateeNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_and_Ratee_but_NoPersnalEval() throws Exception {
        givenEvalSeasonData("EVAL1024", "bkchoi");

        EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectEventsMatching(captureMatcher);

        List<Object> events = captureMatcher.getPayloads();
        PersonalEvaluationCreatedEvent createdEvent = (PersonalEvaluationCreatedEvent) events.get(0);
        assertThat(createdEvent.getRateeType(), equalTo(RateeType.MEMBER));
        assertThat(createdEvent.getFirstRaterId(), equalTo("first"));
        assertThat(createdEvent.getSecondRaterId(), equalTo("second"));

        SelfCompetencyEvaluatedEvent updatedEvent = (SelfCompetencyEvaluatedEvent) events.get(1);
    }

    public class GivenEvalSeason_Ratee_And_PersonalEval {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData("EVAL1024", "bkchoi");
            testExecutor = fixture.given(createEvent());
        }

        @Test
        public void when_Update_SelfEvalDraft_then_Should_Updated() throws Exception {
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            testExecutor
                    .when(updateSelfCompeEvalCmdWithDraft())
                    .expectEventsMatching(captureMatcher);

            SelfCompetencyEvaluatedEvent selfCompeEvalEvent = (SelfCompetencyEvaluatedEvent) captureMatcher.getPayload();
            assertThat(selfCompeEvalEvent.getPersonalEvalId(), equalTo("EVAL1024-bkchoi"));
            assertThat(selfCompeEvalEvent.getEvalSet().isDone(), equalTo(false));

            runInUOW(() -> {
                PersonalEval personalEval = personalEvalRepository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(false));
            });
        }

        @Test
        public void whenUpdateSelfEvalDone_then_Should_Updated() throws Exception {
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            testExecutor
                    .when(updateSelfCompeEvalCmdWithDone())
                    .expectEventsMatching(captureMatcher);

            runInUOW(() -> {
                PersonalEval personalEval = personalEvalRepository.load("EVAL1024-bkchoi");
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(true));
            });
        }

    }

    private void givenNoEvalSeason() {
        when(mockEvalSeasonRepository.load(any())).thenThrow(new AggregateNotFoundException("no", "no"));
    }

    private void givenEvalSeasonData(String evalSeasonId, String... rateeIds) {
        EvalSeason evalSeason = new EvalSeason();
        evalSeason.on(new EvalSeasonCreatedEvent("EVAL1024", "평가", new Date()));
        if (rateeIds.length > 0) {
            List<RateeMapping> mappings = new ArrayList<>();
            for (String rateeId : rateeIds) {
                mappings.add(new RateeMapping(rateeId, RateeType.MEMBER, "first", "second"));
            }
            evalSeason.on(new MappingUpdatedEvent("EVAL1024", mappings));
        }
        when(mockEvalSeasonRepository.load(evalSeasonId)).thenReturn(evalSeason);
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
        return CreationHelper.updateSelfCompeEvalCommand("EVAL1024", "bkchoi", done, hasLeadership, hasAm);
    }

    private PersonalEvaluationCreatedEvent createEvent() {
        return CreationHelper.personalEvalCreatedEvent("EVAL1024", "bkchoi", "first", "second");
    }

}
