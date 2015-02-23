package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.FirstEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.command.personaleval.second.UpdateSecondCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.second.UpdateSecondPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.CommandHelper;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static net.madvirus.eval.axon.AxonUtil.runInUOW;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateSecondPerfCompeEvalCommandHandlerTest {
    public static final String SECOND_ID = "second";
    public static final String EVAL_SEASON_ID = "EVAL2014";
    public static final String RATEE_ID = "ratee";
    private Repository<PersonalEval> mockRepository;
    private UpdateSecondEvalCommandHandler commandHandler;

    @Before
    public void setUp() throws Exception {
        mockRepository = mock(Repository.class);
        commandHandler = new UpdateSecondEvalCommandHandler(mockRepository);
    }

    public class Given_No_RateePersonalEval {
        @Before
        public void setUp() {
            when(mockRepository.load(any())).thenThrow(new AggregateNotFoundException("", ""));
        }

        @Test(expected = PersonalEvalNotFoundException.class)
        public void when_Evaluate_Perf_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateSecondPerfEvalCommandWithSecondRater(SECOND_ID));
        }

        @Test(expected = PersonalEvalNotFoundException.class)
        public void when_Evaluate_Compe_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateSecondCompeEvalCommandWithSecondRater(SECOND_ID));
        }
    }

    public class Given_RateePersonalEval_But_Not_SecondRater {
        @Before
        public void setUp() {
            when(mockRepository.load(any())).thenReturn(createPersonalEvalNotYetFirstDone(EVAL_SEASON_ID, RATEE_ID));
        }

        @Test(expected = YouAreNotSecondRaterException.class)
        public void when_Evaluate_Perf_then_Should_Throw_ex() {
            commandHandler.handle(updateSecondPerfEvalCommandWithSecondRater("nosecond"));
        }

        @Test(expected = YouAreNotSecondRaterException.class)
        public void when_Evaluate_Compe_then_Should_Throw_ex() {
            commandHandler.handle(updateSecondCompeEvalCommandWithSecondRater("nosecond"));
        }
    }

    public class Given_RateePersonalEval_But_NotYesFirstEvalDone {
        @Before
        public void setUp() {
            when(mockRepository.load(any())).thenReturn(createPersonalEvalNotYetFirstDone(EVAL_SEASON_ID, RATEE_ID));
        }

        @Test(expected = FirstEvalNotYetFinishedException.class)
        public void when_Evaluate_SecondPerfEval_then_Should_Throw_ex() {
            commandHandler.handle(updateSecondPerfEvalCommandWithSecondRater(SECOND_ID));
        }
    }

    public class Given_RateePersonalEval_And_FirstEvalDone {

        private PersonalEval personalEval;

        @Before
        public void setUp() {
            personalEval = createPersonalEvalWithFirstDone(EVAL_SEASON_ID, RATEE_ID);
            when(mockRepository.load(any())).thenReturn(personalEval);
        }

        @Test
        public void when_Evaluate_SecondPerfEval_then_Should_Update_SecondPerfEval() {
            runInUOW(() -> {
                commandHandler.handle(updateSecondPerfEvalCommandWithSecondRater(SECOND_ID));
            });
            assertThat(personalEval.getSecondPerfEvalSet().isPresent(), equalTo(true));
        }

        @Test
        public void when_Evaluate_SecondCompeEval_then_Should_Update_SecondCompeEval() throws Exception {
            runInUOW(() -> {
                commandHandler.handle(updateSecondCompeEvalCommandWithSecondRater(SECOND_ID));
            });
            assertThat(personalEval.getSecondCompeEvalSet().isPresent(), equalTo(true));
        }

    }

    private PersonalEval createPersonalEvalNotYetFirstDone(String evalSeasonId, String rateeId) {
        return PersonalEvalHelper.createPersonalEvalWithSelfDone(evalSeasonId, rateeId, true, false);
    }

    private PersonalEval createPersonalEvalWithFirstDone(String evalSeasonId, String rateeId) {
        return PersonalEvalHelper.createPersonalEvalWithSelfDone(evalSeasonId, rateeId, true, true);
    }

    private UpdateSecondPerformanceEvalCommand updateSecondPerfEvalCommandWithSecondRater(String secondRaterId) {
        return CommandHelper.updateSecondPerfEvalCommandWithRater(secondRaterId);
    }

    private UpdateSecondCompetencyEvalCommand updateSecondCompeEvalCommandWithSecondRater(String second) {
        return CommandHelper.updateSecondCompeEvalCommand(EVAL_SEASON_ID, RATEE_ID, second, false, false, false);
    }

}
