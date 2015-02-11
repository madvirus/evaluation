package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.api.personaleval.first.*;
import net.madvirus.eval.api.personaleval.self.SelfCompetencyEvaluatedEvent;
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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Nested.class)
public class UpdateFirstCompetenceEvalCommandTest {
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
                    .when(updateFirstCompeEvalCommandWithFirstRater("first"))
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
                    .when(updateFirstCompeEvalCommandWithFirstRater("nofirst"))
                    .expectException(YouAreNotFirstRaterException.class);
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Throw_Ex() throws Exception {
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater("first"))
                    .expectException(SelfEvalNotYetFinishedException.class);
        }
    }

    public class Given_RateePersonalEval_And_SelfEvaluationDone {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(
                    createEventWithFirstRater("first"),
                    selfCompeEvaluatedEventWithDone());
        }

        @Test
        public void when_FirstRater_Evaluates_then_Should_Publish_EvaluatedEvent() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(updateFirstCompeEvalCommandWithFirstRater("first"))
                    .expectEventsMatching(capture);
            FirstCompetencyEvaluatedEvent event = (FirstCompetencyEvaluatedEvent) capture.getPayload();

            assertThat(event.getPersonalEvalId(), equalTo("EVAL2014-ratee"));
            assertThat(event.getEvalSet().getTotalEval().getGrade(), equalTo(Grade.A));
        }

        @Test
        public void when_FirstRater_Reject_then_SelfCompeEval_Should_Return_To_Progressing() {
            EventCaptureMatcher capture = new EventCaptureMatcher();
            testExecutor
                    .when(rejectSelfCompeEvalCommandWithFirstRater("first"))
                    .expectEventsMatching(capture);

            SelfCompetencyEvalRejectedEvent event = (SelfCompetencyEvalRejectedEvent) capture.getPayload();
            assertThat(event.getPersonalEvalId(), equalTo(PersonalEval.createId("EVAL2014", "ratee")));

            AxonUtil.runInUOW(() -> {
                PersonalEval personalEval = repository.load(PersonalEval.createId("EVAL2014", "ratee"));
                assertThat(personalEval.isSelfCompeEvalDone(), equalTo(false));
            });
        }

        private RejectSelfCompetencyEvalCommand rejectSelfCompeEvalCommandWithFirstRater(String first) {
            return new RejectSelfCompetencyEvalCommand("EVAL2014", "ratee", first);
        }
    }

    private UpdateFirstCompetencyEvalCommand updateFirstCompeEvalCommandWithFirstRater(String firstRaterId) {
        return CreationHelper.updateFirstCompeEvalCommand("EVAL2014", "ratee", firstRaterId, false, false, false);
    }

    private PersonalEvaluationCreatedEvent createEventWithFirstRater(String firstRaterId) {
        return new PersonalEvaluationCreatedEvent("EVAL2014-ratee", "EVAL2014", "ratee", RateeType.MEMBER, firstRaterId, "second");
    }

    private SelfCompetencyEvaluatedEvent selfCompeEvaluatedEventWithDone() {
        return new SelfCompetencyEvaluatedEvent(
                "EVAL2014-ratee",new CompetencyEvalSet(null, null, null, null, true)
        );
    }


}
