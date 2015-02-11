package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.colleague.UpdateColleagueCompetencyEvalCommand;
import net.madvirus.eval.api.personaleval.colleague.YouAreNotColleagueRaterException;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateColleagueCompetencyEvalCommandTest {
    protected FixtureConfiguration fixture;
    private Repository<PersonalEval> personalEvalRepository;
    private Repository<EvalSeason> mockEvalSeasonRepository;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        personalEvalRepository = fixture.getRepository();
        mockEvalSeasonRepository = mock(Repository.class);
        fixture.registerAnnotatedCommandHandler(new UpdateColleagueEvalCommandHandler(personalEvalRepository, mockEvalSeasonRepository));
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
        givenEvalSeasonData("EVAL1024");

        fixture.given()
                .when(updateColleagueCompeEvalCmdWithDraft())
                .expectException(RateeNotFoundException.class);
    }

    @Test
    public void givenEvalSeason_and_Ratee_but_NoPersonalEval_thenThrow_ex() throws Exception {
        givenEvalSeasonData("EVAL1024", "bkchoi");

        fixture.given()
                .when(updateColleagueCompeEvalCmdWithDraft())
                .expectException(PersonalEvalNotFoundException.class);
    }

    public class Given_EvalSeason_Ratee_PersonalEval {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData("EVAL1024", "bkchoi");
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
            assertThat(event.getPersonalEvalId(), equalTo("EVAL1024-bkchoi"));
            assertThat(event.getColleagueRaterId(), equalTo("colleague"));
        }
    }
    public class Given_EvalSeason_Ratee_PersonalEval_AlreadyEvalDone {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            givenEvalSeasonData("EVAL1024", "bkchoi");
            testExecutor = fixture.given(
                    createPersonalEvalEvent(),
                    new ColleagueCompetencyEvaluatedEvent("EVAL1024-bkchoi", "colleague",
                            new CompetencyEvalSet(null, null, null, null, true))
                    );
        }

        @Test
        public void when_ColleagueRater_Evaluate_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateColleagueCompeEvalCmdWithDraft())
                    .expectException(AlreadyEvaluationDoneException.class);
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
        return createUpdateCommand(done, true, false, "colleague");
    }

    private UpdateColleagueCompetencyEvalCommand createUpdateCommand(boolean done, boolean hasLeadership, boolean hasAm, String colleagueId) {
        return CreationHelper.updateColleagueCompeEvalCommand(
                "EVAL1024", "bkchoi", colleagueId, done, hasLeadership, hasAm);
    }

    private PersonalEvaluationCreatedEvent createPersonalEvalEvent() {
        return new PersonalEvaluationCreatedEvent("EVAL1024-bkchoi", "EVAL1024", "bkchoi", RateeType.MEMBER, "first", "second");
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
                mappings.add(new RateeMapping(rateeId, RateeType.MEMBER, "first", "second", "colleague"));
            }
            evalSeason.on(new MappingUpdatedEvent("EVAL1024", mappings));
        }
        when(mockEvalSeasonRepository.load(evalSeasonId)).thenReturn(evalSeason);
    }


}
