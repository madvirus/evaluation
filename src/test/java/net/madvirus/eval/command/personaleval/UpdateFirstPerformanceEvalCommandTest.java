package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.first.*;
import net.madvirus.eval.api.personaleval.self.SelfPerformanceEvaluatedEvent;
import net.madvirus.eval.axon.AxonUtil;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.testhelper.CreationHelper;
import org.axonframework.repository.Repository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(Nested.class)
public class UpdateFirstPerformanceEvalCommandTest {
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
                    .when(updateFirstPerfEvalCommandWithFirstRater("first"))
                    .expectException(PersonalEvalNotFoundException.class);
        }
    }

    public class Given_RateePersonalEval_And_NotYet_SelfEvaluation_Done {
        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(createEventWithFirstRater("first"));
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
                    .when(updateFirstPerfEvalCommandWithFirstRater("first"))
                    .expectException(SelfEvalNotYetFinishedException.class);
        }

    }

    public class Given_RateePersonalEval_And_SelfEvaluationDone {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(
                    createEventWithFirstRater("first"),
                    selfPerfEvaluatedEventWithDone());
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Publish_EvaluatedEvent() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(updateFirstPerfEvalCommandWithFirstRater("first"))
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
                    .when(rejectSelfPerfEvalCommandWithFirstRater("first"))
                    .expectEventsMatching(capture);

            SelfPerformanceEvalRejectedEvent event = (SelfPerformanceEvalRejectedEvent) capture.getPayload();
            assertThat(event.getPersonalEvalId(), equalTo(PersonalEval.createId("EVAL2014", "ratee")));

            AxonUtil.runInUOW(() -> {
                PersonalEval personalEval = repository.load(PersonalEval.createId("EVAL2014", "ratee"));
                assertThat(personalEval.isSelfPerfEvalDone(), equalTo(false));
            });
        }

        private RejectSelfPerformanceEvalCommand rejectSelfPerfEvalCommandWithFirstRater(String first) {
            return new RejectSelfPerformanceEvalCommand("EVAL2014", "ratee", first);
        }
    }


    private UpdateFirstPerformanceEvalCommand updateFirstPerfEvalCommandWithFirstRater(String firstRaterId) {
        return new UpdateFirstPerformanceEvalCommand(
                "EVAL2014", "ratee", firstRaterId,
                Arrays.asList(new ItemEval("first comment", Grade.B)),
                new ItemEval("total comment", Grade.B));
    }

    private PersonalEvaluationCreatedEvent createEventWithFirstRater(String firstRaterId) {
        return CreationHelper.personalEvalCreatedEvent("EVAL2014", "ratee", firstRaterId, "second");
    }

    private SelfPerformanceEvaluatedEvent selfPerfEvaluatedEventWithDone() {
        return CreationHelper.selfPerfEvaluatedEvent("EVAL2014", "ratee", true);
    }

}
