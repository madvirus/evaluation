package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.command.personaleval.self.UpdateSelfCompetencyEvalCommand;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.AlreadySelfCompetencyEvalDoneException;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.CommandHelper;
import net.madvirus.eval.testhelper.EventHelper;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
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
    public static final String EVALSEASON_ID = "EVAL1024";
    public static final String RATEE_ID = "bkchoi";
    public static final String FIRST_ID = "first";
    public static final String SECOND_ID = "second";
    public static final String PERSONAL_EVAL_ID = "EVAL1024-bkchoi";
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
        givenEvalSeasonData(EVALSEASON_ID);

        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectException(RateeNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_and_Ratee_but_NoPersnalEval() throws Exception {
        givenEvalSeasonData(EVALSEASON_ID, RATEE_ID);

        EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

        fixture.given()
                .when(updateSelfCompeEvalCmdWithDraft())
                .expectEventsMatching(captureMatcher);

        List<Object> events = captureMatcher.getPayloads();
        PersonalEvaluationCreatedEvent createdEvent = (PersonalEvaluationCreatedEvent) events.get(0);
        assertThat(createdEvent.getRateeType(), equalTo(RateeType.MEMBER));
        assertThat(createdEvent.getFirstRaterId(), equalTo(FIRST_ID));
        assertThat(createdEvent.getSecondRaterId(), equalTo(SECOND_ID));

        SelfCompetencyEvaluatedEvent updatedEvent = (SelfCompetencyEvaluatedEvent) events.get(1);
    }

    public class GivenEvalSeason_Ratee_And_PersonalEval {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData(EVALSEASON_ID, RATEE_ID);
            testExecutor = fixture.given(createEvent());
        }

        @Test
        public void when_Update_SelfEvalDraft_then_Should_Updated() throws Exception {
            EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

            testExecutor
                    .when(updateSelfCompeEvalCmdWithDraft())
                    .expectEventsMatching(captureMatcher);

            SelfCompetencyEvaluatedEvent selfCompeEvalEvent = (SelfCompetencyEvaluatedEvent) captureMatcher.getPayload();
            assertThat(selfCompeEvalEvent.getPersonalEvalId(), equalTo(PERSONAL_EVAL_ID));
            assertThat(selfCompeEvalEvent.getEvalSet().isDone(), equalTo(false));

            runInUOW(() -> {
                PersonalEval personalEval = personalEvalRepository.load(PERSONAL_EVAL_ID);
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
                PersonalEval personalEval = personalEvalRepository.load(PERSONAL_EVAL_ID);
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(true));
            });
        }
    }

    public class GivenAlreadyCompeEvalDone {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData(EVALSEASON_ID, RATEE_ID);
            testExecutor = fixture.given(
                    createEvent(),
                    EventHelper.selfCompeEvaluatedEvent(EVALSEASON_ID, RATEE_ID, true));
        }

        @Test
        public void whenUpdate_then_Should_Throw_ex() {
            testExecutor
                    .when(updateSelfCompeEvalCmdWithDraft())
                    .expectException(AlreadySelfCompetencyEvalDoneException.class);
        }
    }

    private void givenNoEvalSeason() {
        when(mockEvalSeasonRepository.load(any())).thenThrow(new AggregateNotFoundException("no", "no"));
    }

    private void givenEvalSeasonData(String evalSeasonId, String... rateeIds) {
        EvalSeason evalSeason = new EvalSeason();
        evalSeason.on(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()));
        if (rateeIds.length > 0) {
            List<RateeMapping> mappings = new ArrayList<>();
            for (String rateeId : rateeIds) {
                mappings.add(new RateeMapping(rateeId, RateeType.MEMBER, FIRST_ID, SECOND_ID));
            }
            evalSeason.on(new MappingUpdatedEvent(EVALSEASON_ID, mappings));
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
        return CommandHelper.updateSelfCompeEvalCommand(EVALSEASON_ID, RATEE_ID, done, hasLeadership, hasAm);
    }

    private PersonalEvaluationCreatedEvent createEvent() {
        return EventHelper.personalEvalCreatedEvent(EVALSEASON_ID, RATEE_ID, FIRST_ID, SECOND_ID);
    }

}
