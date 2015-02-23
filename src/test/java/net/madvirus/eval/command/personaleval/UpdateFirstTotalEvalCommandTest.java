package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.first.NotYetFirstPerfOrCompeEvalDoneException;
import net.madvirus.eval.api.personaleval.first.YouAreNotFirstRaterException;
import net.madvirus.eval.axon.AxonUtil;
import net.madvirus.eval.command.personaleval.first.UpdateFirstTotalEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.CommandHelper;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateFirstTotalEvalCommandTest {

    public static final String EVAL_SEASON_ID = "EVAL2014";
    public static final String RATEE1_ID = "ratee1";
    public static final String RATEE2_ID = "ratee2";
    public static final String FIRST_ID = "first";
    private Repository<PersonalEval> repository = mock(Repository.class);
    private UpdateFirstEvalCommandHandler commandHandler;

    @Before
    public void setUp() throws Exception {
        commandHandler = new UpdateFirstEvalCommandHandler(repository);
    }

    public class Given_No_RateePersonalEval {
        @Before
        public void setUp() throws Exception {
            when(repository.load(any())).thenThrow(new AggregateNotFoundException("", ""));
        }

        @Test(expected = PersonalEvalNotFoundException.class)
        public void when_Evaluate_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateCommandWithFirstRater(FIRST_ID));
        }
    }

    public class Given_RateePersonalEval_But_Not_FirstRater {
        @Before
        public void setUp() {
            PersonalEval ratee1Eval = createPersonalEval(EVAL_SEASON_ID, RATEE1_ID, false);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(ratee1Eval);

            PersonalEval ratee2Eval = createPersonalEval(EVAL_SEASON_ID, RATEE2_ID, false);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(ratee2Eval);
        }

        @Test(expected = YouAreNotFirstRaterException.class)
        public void when_Evaluate_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateCommandWithFirstRater("noFirst"));
        }

    }

    public class Given_RateePersonalEval_And_FirstRater_But_NotYet_First_Perf_Compe_Eval_Had {
        @Before
        public void setUp() {
            PersonalEval ratee1Eval = createPersonalEval(EVAL_SEASON_ID, RATEE1_ID, false);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(ratee1Eval);

            PersonalEval ratee2Eval = createPersonalEval(EVAL_SEASON_ID, RATEE2_ID, false);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(ratee2Eval);
        }

        @Test(expected = NotYetFirstPerfOrCompeEvalDoneException.class)
        public void when_Evaluate_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateCommandWithFirstRater(FIRST_ID));
        }
    }

    public class Given_RateePersonalEval_And_FirstRater_And_First_Perf_Compe_Eval_Had {

        private PersonalEval ratee1Eval;
        private PersonalEval ratee2Eval;

        @Before
        public void setUp() {
            ratee1Eval = createPersonalEval(EVAL_SEASON_ID, RATEE1_ID, true);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(ratee1Eval);

            ratee2Eval = createPersonalEval(EVAL_SEASON_ID, RATEE2_ID, true);
            when(repository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(ratee2Eval);
        }

        @Test
        public void when_Evaluate_Draft_then_First_Eval_Should_Updated() {
            AxonUtil.runInUOW(() -> {
                commandHandler.handle(updateCommandWithFirstRater(FIRST_ID));
            });

            assertThat(ratee1Eval.getFirstTotalEval().get().isDone(), equalTo(false));
            assertThat(ratee1Eval.getFirstTotalEval().get().getComment(), equalTo("comment1"));
            assertThat(ratee1Eval.getFirstPerfEvalSet().get().isDone(), equalTo(false));
            assertThat(ratee1Eval.getFirstCompeEvalSet().get().isDone(), equalTo(false));

        }

        @Test
        public void when_Evaluate_Done_then_First_Eval_Should_Done() {
            AxonUtil.runInUOW(() -> {
                commandHandler.handle(updateDoneCommandWithFirstRater(FIRST_ID));
            });

            firstEvalAllDone(ratee1Eval);
            firstEvalAllDone(ratee2Eval);
        }

        private void firstEvalAllDone(PersonalEval rateeEval) {
            assertThat(rateeEval.getFirstTotalEval().get().isDone(), equalTo(true));
            assertThat(rateeEval.getFirstPerfEvalSet().get().isDone(), equalTo(true));
            assertThat(rateeEval.getFirstCompeEvalSet().get().isDone(), equalTo(true));
        }

    }

    private PersonalEval createPersonalEval(String evalSeasonId, String rateeId, boolean firstPerfCompeHad) {
        return PersonalEvalHelper.createPersonalEvalWithSelfDone(evalSeasonId, rateeId, firstPerfCompeHad, false);
    }

    private UpdateFirstTotalEvalCommand updateCommandWithFirstRater(String first) {
        return updateCommand(first, false, RATEE1_ID, RATEE2_ID);
    }

    private UpdateFirstTotalEvalCommand updateDoneCommandWithFirstRater(String first) {
        return updateCommand(first, true, RATEE1_ID, RATEE2_ID);
    }

    private UpdateFirstTotalEvalCommand updateCommand(String first, boolean done, String ... rateeIds) {
        return CommandHelper.updateFirstTotalEvalCommand(EVAL_SEASON_ID, first, done, rateeIds);
    }

}
