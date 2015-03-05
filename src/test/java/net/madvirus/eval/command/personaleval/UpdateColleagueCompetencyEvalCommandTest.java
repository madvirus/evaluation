package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.EvalSeasonNotFoundException;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.api.evalseaon.RateeNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.command.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.domain.evalseason.EvalSeason;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.AlreadyColleagueEvalDoneException;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.CommandHelper;
import net.madvirus.eval.testhelper.EventHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateColleagueCompetencyEvalCommandTest {
    public static final String EVAL_SEASON_ID = "EVAL1024";
    public static final String RATEE_ID = "bkchoi";
    public static final String PERSONAL_EVAL_ID = PersonalEval.createId(EVAL_SEASON_ID, RATEE_ID);
    public static final String COLLEAGUE_ID = "colleague";
    public static final String FIRST_RATER = "first";
    public static final String SECOND_ID = "second";
    protected FixtureConfiguration fixture;
    private Repository<EvalSeason> mockEvalSeasonRepository;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        mockEvalSeasonRepository = mock(Repository.class);
        fixture.registerAnnotatedCommandHandler(new UpdateColleagueEvalCommandHandler(fixture.getRepository(), mockEvalSeasonRepository));
    }

    @Test
    public void givenNoEvalSeason_thenThrow_ex() throws Exception {
        givenNoEvalSeason();
        fixture.given()
                .when(updateColleagueCompeEvalCmdWithDraft())
                .expectException(EvalSeasonNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_but_NoRatee_thenThrow_ex() throws Exception {
        givenEvalSeasonData(EVAL_SEASON_ID);

        fixture.given()
                .when(updateColleagueCompeEvalCmdWithDraft())
                .expectException(RateeNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_and_Ratee_but_NoPersonalEval_thenThrow_ex() throws Exception {
        givenEvalSeasonData(EVAL_SEASON_ID, RATEE_ID);

        fixture.given()
                .when(updateColleagueCompeEvalCmdWithDraft())
                .expectException(PersonalEvalNotFoundException.class);
    }

    public class Given_EvalSeason_Ratee_PersonalEval {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData(EVAL_SEASON_ID, RATEE_ID);
            testExecutor = fixture.given(createPersonalEvalEvent());
        }

        @Test
        public void when_NotColleagueRater_Evaluate_then_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateColleagueCompeEvalCmdWithDraft("notColleague"))
                    .expectException(YouAreNotColleagueRaterException.class);
        }

        @Test
        public void when_ColleagueRater_Evaluate_then_Should_Public_EvaluatedEvent() throws Exception {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(updateColleagueCompeEvalCmdWithDraft())
                    .expectEventsMatching(capture);

            ColleagueCompetencyEvaluatedEvent event = (ColleagueCompetencyEvaluatedEvent) capture.getPayload();
            assertThat(event.getPersonalEvalId(), equalTo(PERSONAL_EVAL_ID));
            assertThat(event.getColleagueRaterId(), equalTo(COLLEAGUE_ID));
        }
    }
    public class Given_EvalSeason_Ratee_PersonalEval_AlreadyEvalDone {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData(EVAL_SEASON_ID, RATEE_ID);
            testExecutor = fixture.given(
                    createPersonalEvalEvent(),
                    new ColleagueCompetencyEvaluatedEvent(PERSONAL_EVAL_ID, COLLEAGUE_ID,
                            new CompetencyEvalSet(null, null, null, null, true))
                    );
        }

        @Test
        public void when_ColleagueRater_Evaluate_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateColleagueCompeEvalCmdWithDraft())
                    .expectException(AlreadyColleagueEvalDoneException.class);
        }

    }

    private UpdateColleagueCompetencyEvalCommand updateColleagueCompeEvalCmdWithDraft() {
        return createUpdateCommand(false);
    }

    private UpdateColleagueCompetencyEvalCommand updateColleagueCompeEvalCmdWithDraft(String colleagueId) {
        return createUpdateCommand(false, true, false, colleagueId);
    }

    private UpdateColleagueCompetencyEvalCommand updateColleagueCompeEvalCmdWithDone() {
        return createUpdateCommand(true);
    }

    private UpdateColleagueCompetencyEvalCommand createUpdateCommand(boolean done) {
        return createUpdateCommand(done, true, false, COLLEAGUE_ID);
    }

    private UpdateColleagueCompetencyEvalCommand createUpdateCommand(boolean done, boolean hasLeadership, boolean hasAm, String colleagueId) {
        return CommandHelper.updateColleagueCompeEvalCommand(
                EVAL_SEASON_ID, RATEE_ID, colleagueId, done, hasLeadership, hasAm);
    }

    private PersonalEvaluationCreatedEvent createPersonalEvalEvent() {
        return EventHelper.personalEvalCreatedEvent(EVAL_SEASON_ID, RATEE_ID, FIRST_RATER, SECOND_ID);
    }

    private void givenNoEvalSeason() {
        when(mockEvalSeasonRepository.load(any())).thenThrow(new AggregateNotFoundException("no", "no"));
    }

    private void givenEvalSeasonData(String evalSeasonId, String... rateeIds) {
        EvalSeason evalSeason = new EvalSeason();
        evalSeason.on(new EvalSeasonCreatedEvent(EVAL_SEASON_ID, "평가", new Date()));
        if (rateeIds.length > 0) {
            List<RateeMapping> mappings = new ArrayList<>();
            for (String rateeId : rateeIds) {
                mappings.add(new RateeMapping(rateeId, RateeType.MEMBER, FIRST_RATER, SECOND_ID, COLLEAGUE_ID));
            }
            evalSeason.on(new MappingUpdatedEvent(EVAL_SEASON_ID, mappings));
        }
        when(mockEvalSeasonRepository.load(evalSeasonId)).thenReturn(evalSeason);
    }


}
