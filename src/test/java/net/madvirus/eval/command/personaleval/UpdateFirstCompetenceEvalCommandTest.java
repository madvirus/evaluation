package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.first.FirstCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.SelfCompetencyEvalRejectedEvent;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
import net.madvirus.eval.axon.AxonUtil;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.command.personaleval.common.UpdateRaterCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.first.RejectSelfCompetencyEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.CommandHelper;
import net.madvirus.eval.testhelper.EventHelper;
import org.axonframework.repository.Repository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Nested.class)
public class UpdateFirstCompetenceEvalCommandTest {
    public static final String FIRST_RATER = "first";
    public static final String EVAL_SEASON_ID = "EVAL2014";
    public static final String RATEE_ID = "ratee";
    public static final String SECOND = "second";
    public static final String PERSONAL_EVAL_ID = "EVAL2014-ratee";
    protected FixtureConfiguration fixture;
    private Repository<PersonalEval> repository;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        repository = fixture.getRepository();
        fixture.registerAnnotatedCommandHandler(new UpdateFirstEvalCommandHandler(repository));
        fixture.registerAnnotatedCommandHandler(new RejectEvalCommandHandler(repository));
    }

    public class Given_No_RateePersonalEval {
        private TestExecutor testExecutor;

        @Before
        public void setUp() {
            testExecutor = fixture.given();
        }

        @Test
        public void when_Evaluate_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater(FIRST_RATER))
                    .expectException(PersonalEvalNotFoundException.class);
        }
    }

    public class Given_RateePersonalEval_And_NotYet_SelfEvaluation_Done {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(createEventWithFirstRater(FIRST_RATER));
        }

        @Test
        public void when_NotFirstRater_Evaluates_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater("nofirst"))
                    .expectException(YouAreNotFirstRaterException.class);
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater(FIRST_RATER))
                    .expectException(SelfEvalNotYetFinishedException.class);
        }
    }

    public class Given_RateePersonalEval_And_SelfEvaluationDone {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(
                    createEventWithFirstRater(FIRST_RATER),
                    selfCompeEvaluatedEventWithDone());
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Publish_EvaluatedEvent() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater(FIRST_RATER))
                    .expectEventsMatching(capture);
            FirstCompetencyEvaluatedEvent event = (FirstCompetencyEvaluatedEvent) capture.getPayload();

            assertThat(event.getPersonalEvalId(), equalTo(PERSONAL_EVAL_ID));
            assertThat(event.getEvalSet().getTotalEval().getGrade(), equalTo(Grade.A));
        }

        @Test
        public void when_FirstRater_Reject_then_SelfCompeEval_Should_Return_To_Progressing() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(rejectSelfCompeEvalCommandWithFirstRater(FIRST_RATER))
                    .expectEventsMatching(capture);

            SelfCompetencyEvalRejectedEvent event = (SelfCompetencyEvalRejectedEvent) capture.getPayload();
            assertThat(event.getPersonalEvalId(), equalTo(PersonalEval.createId(EVAL_SEASON_ID, RATEE_ID)));

            AxonUtil.runInUOW(() -> {
                PersonalEval personalEval = repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE_ID));
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(false));
            });
        }

        private RejectSelfCompetencyEvalCommand rejectSelfCompeEvalCommandWithFirstRater(String first) {
            return new RejectSelfCompetencyEvalCommand(EVAL_SEASON_ID, RATEE_ID, first);
        }
    }

    private UpdateRaterCompetencyEvalCommand updateFirstCompeEvalCommandWithFirstRater(String firstRaterId) {
        return CommandHelper.updateFirstCompeEvalCommand(EVAL_SEASON_ID, RATEE_ID, firstRaterId, false, false, false);
    }

    private PersonalEvaluationCreatedEvent createEventWithFirstRater(String firstRaterId) {
        return EventHelper.personalEvalCreatedEvent(EVAL_SEASON_ID, RATEE_ID, firstRaterId, SECOND);
    }

    private SelfCompetencyEvaluatedEvent selfCompeEvaluatedEventWithDone() {
        return EventHelper.selfCompeEvaluatedEvent(EVAL_SEASON_ID, RATEE_ID, true);
    }


}
