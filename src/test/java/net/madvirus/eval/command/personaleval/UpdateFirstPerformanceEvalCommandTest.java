package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.first.FirstPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.first.SelfPerformanceEvalRejectedEvent;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.api.personaleval.self.SelfPerformanceEvaluatedEvent;
import net.madvirus.eval.axon.AxonUtil;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.command.personaleval.first.RejectSelfPerformanceEvalCommand;
import net.madvirus.eval.command.personaleval.first.UpdateFirstPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.ItemEval;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(Nested.class)
public class UpdateFirstPerformanceEvalCommandTest {
    public static final String FIRST_ID = "first";
    public static final String EVAL_SEASON_ID = "EVAL2014";
    public static final String RATEE_ID = "ratee";
    public static final String SECOND_ID = "second";
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
                    .when(updateFirstPerfEvalCommandWithFirstRater(FIRST_ID))
                    .expectException(PersonalEvalNotFoundException.class);
        }
    }

    public class Given_RateePersonalEval_And_NotYet_SelfEvaluation_Done {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(createEventWithFirstRater(FIRST_ID));
        }

        @Test
        public void when_NotFirstRater_Evaluates_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstPerfEvalCommandWithFirstRater("nofirst"))
                    .expectException(YouAreNotFirstRaterException.class);
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstPerfEvalCommandWithFirstRater(FIRST_ID))
                    .expectException(SelfEvalNotYetFinishedException.class);
        }

    }

    public class Given_RateePersonalEval_And_SelfEvaluationDone {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(
                    createEventWithFirstRater(FIRST_ID),
                    selfPerfEvaluatedEventWithDone());
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Publish_EvaluatedEvent() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(updateFirstPerfEvalCommandWithFirstRater(FIRST_ID))
                    .expectEventsMatching(capture);
            FirstPerformanceEvaluatedEvent event = (FirstPerformanceEvaluatedEvent) capture.getPayload();

            assertThat(event.getPersonalEvalId(), equalTo("EVAL2014-ratee"));
            assertThat(event.getTotalEval(), notNullValue());
            List<ItemEval> itemEvals = event.getItemEvals();
            assertThat(itemEvals, hasSize(1));
            assertThat(itemEvals.get(0).getComment(), equalTo("first comment"));
            assertThat(itemEvals.get(0).getGrade(), equalTo(Grade.B));
        }

        @Test
        public void when_FirstRater_Return_then_SelfPerfEval_Should_Return_To_Progressing() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(rejectSelfPerfEvalCommandWithFirstRater(FIRST_ID))
                    .expectEventsMatching(capture);

            SelfPerformanceEvalRejectedEvent event = (SelfPerformanceEvalRejectedEvent) capture.getPayload();
            assertThat(event.getPersonalEvalId(), equalTo(PersonalEval.createId(EVAL_SEASON_ID, RATEE_ID)));

            AxonUtil.runInUOW(() -> {
                PersonalEval personalEval = repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE_ID));
                assertThat(personalEval.isSelfPerfEvalDone(), equalTo(false));
            });
        }

        private RejectSelfPerformanceEvalCommand rejectSelfPerfEvalCommandWithFirstRater(String first) {
            return new RejectSelfPerformanceEvalCommand(EVAL_SEASON_ID, RATEE_ID, first);
        }
    }


    private UpdateFirstPerformanceEvalCommand updateFirstPerfEvalCommandWithFirstRater(String firstRaterId) {
        return CommandHelper.updateFirstPerfEvalCommandWithRater(firstRaterId);
    }

    private PersonalEvaluationCreatedEvent createEventWithFirstRater(String firstRaterId) {
        return EventHelper.personalEvalCreatedEvent(EVAL_SEASON_ID, RATEE_ID, firstRaterId, SECOND_ID);
    }

    private SelfPerformanceEvaluatedEvent selfPerfEvaluatedEventWithDone() {
        return EventHelper.selfPerfEvaluatedEvent(EVAL_SEASON_ID, RATEE_ID, true);
    }

}
