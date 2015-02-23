package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.personaleval.AlreadyEvaluationDoneException;
import net.madvirus.eval.api.personaleval.PersonalEvalNotFoundException;
import net.madvirus.eval.api.personaleval.second.NotYetSecondPerfOrCompeEvalDoneException;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.command.personaleval.second.UpdateSecondTotalEvalCommand;
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
public class UpdateSecondTotalEvalCommandHandlerTest {
    public static final String EVAL_SEASON_ID = "EVAL2014";
    public static final String RATEE_ID = "ratee";
    public static final String RATEE1_ID = "ratee1";
    public static final String RATEE2_ID = "ratee2";
    public static final String SECOND_ID = "second";
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
        public void when_Evaluate_then_Should_Throw_Ex() throws Exception {
            commandHandler.handle(updateSecondTotalDoneCommandWithRater(SECOND_ID));
        }
    }

    public class Given_RateePersonalEval_But_Not_SecondRater {
        @Before
        public void setUp() {
            when(mockRepository.load(any())).thenReturn(createPersonalEvalWithFirstDone(EVAL_SEASON_ID, RATEE_ID, true));
        }

        @Test(expected = YouAreNotSecondRaterException.class)
        public void when_Evaluate_then_Should_Throw_ex() {
            commandHandler.handle(updateSecondTotalDoneCommandWithRater("nosecond"));
        }
    }

    public class Given_RateePersonalEval_But_NotYesSecondPerfCompeEvalHad {
        @Before
        public void setUp() {
            PersonalEval personalEval1 = createPersonalEvalWithFirstDoneAndNoSecondEvalHad(EVAL_SEASON_ID, RATEE1_ID);
            PersonalEval personalEval2 = createPersonalEvalWithFirstDoneAndNoSecondEvalHad(EVAL_SEASON_ID, RATEE2_ID);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(personalEval1);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(personalEval2);
        }

        @Test(expected = NotYetSecondPerfOrCompeEvalDoneException.class)
        public void when_Evaluate_then_Should_Throw_ex() {
            commandHandler.handle(updateSecondTotalDoneCommandWithRater(SECOND_ID));
        }
    }

    public class Given_RateePersonalEval_And_SecondPerfCompeEvalHad {
        private PersonalEval personalEval1;
        private PersonalEval personalEval2;

        @Before
        public void setUp() {
            personalEval1 = createPersonalEvalWithSecondEvalHadAndNotYetDone(EVAL_SEASON_ID, RATEE1_ID);
            personalEval2 = createPersonalEvalWithSecondEvalHadAndNotYetDone(EVAL_SEASON_ID, RATEE2_ID);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(personalEval1);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(personalEval2);
        }

        @Test
        public void when_Evaluate_Draft_then_TotalEval_Should_Be_Update() {
            commandHandler.handle(updateSecondTotalDraftCommandWithRater(SECOND_ID));
            assertThat(personalEval1.getSecondTotalEval().isPresent(), equalTo(true));
            assertThat(personalEval1.getSecondTotalEval().get().isDone(), equalTo(false));
            assertThat(personalEval2.getSecondTotalEval().isPresent(), equalTo(true));
            assertThat(personalEval2.getSecondTotalEval().get().isDone(), equalTo(false));
        }

        @Test
        public void when_Evaluate_Done_then_TotalEval_All_Should_Be_Done() {
            commandHandler.handle(updateSecondTotalDoneCommandWithRater(SECOND_ID));
            assertSecondDone(personalEval1);
            assertSecondDone(personalEval2);
        }

        private void assertSecondDone(PersonalEval personalEval) {
            assertThat(personalEval.getSecondTotalEval().isPresent(), equalTo(true));
            assertThat(personalEval.getSecondTotalEval().get().isDone(), equalTo(true));
            assertThat(personalEval.getSecondPerfEvalSet().get().isDone(), equalTo(true));
            assertThat(personalEval.getSecondCompeEvalSet().get().isDone(), equalTo(true));
        }
    }

    public class Given_RateePersonalEval_And_SecondTotalEvalDone {
        private PersonalEval personalEval1;
        private PersonalEval personalEval2;

        @Before
        public void setUp() {
            personalEval1 = createPersonalEvalWithSecondEvalDone(EVAL_SEASON_ID, RATEE1_ID);
            personalEval2 = createPersonalEvalWithSecondEvalDone(EVAL_SEASON_ID, RATEE2_ID);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE1_ID))).thenReturn(personalEval1);
            when(mockRepository.load(PersonalEval.createId(EVAL_SEASON_ID, RATEE2_ID))).thenReturn(personalEval2);
        }

        @Test(expected = AlreadyEvaluationDoneException.class)
        public void when_Evaluate_then_Should_Throw_Ex() {
            commandHandler.handle(updateSecondTotalDoneCommandWithRater(SECOND_ID));
        }

    }

    private UpdateSecondTotalEvalCommand updateSecondTotalDoneCommandWithRater(String second) {
        return CommandHelper.updateSecondTotalEvalCommand(EVAL_SEASON_ID, second, true, RATEE1_ID, RATEE2_ID);
    }

    private UpdateSecondTotalEvalCommand updateSecondTotalDraftCommandWithRater(String second) {
        return CommandHelper.updateSecondTotalEvalCommand(EVAL_SEASON_ID, second, false, RATEE1_ID, RATEE2_ID);
    }


    private PersonalEval createPersonalEvalWithFirstDone(String evalSeasonId, String rateeId, boolean perfCompeHad) {
        return PersonalEvalHelper.createPersonalEvalWithFirstDone(evalSeasonId, rateeId, perfCompeHad);
    }

    private PersonalEval createPersonalEvalWithFirstDoneAndNoSecondEvalHad(String evalSeasonId, String rateeId) {
        return PersonalEvalHelper.createPersonalEvalWithFirstDone(evalSeasonId, rateeId, false);
    }

    private PersonalEval createPersonalEvalWithSecondEvalHadAndNotYetDone(String evalSeasonId, String rateeId) {
        return PersonalEvalHelper.createPersonalEvalWithFirstDone(evalSeasonId, rateeId, true);
    }

    private PersonalEval createPersonalEvalWithSecondEvalDone(String evalSeasonId, String rateeId) {
        return PersonalEvalHelper.createPersonalEvalWithSecondDone(evalSeasonId, rateeId);
    }
}
