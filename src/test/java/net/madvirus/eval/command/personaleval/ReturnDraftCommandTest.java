package net.madvirus.eval.command.personaleval;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.AlreadySecondEvalDoneException;
import net.madvirus.eval.domain.personaleval.AlreadySecondEvalStartedException;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModel;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepositoryImpl;
import net.madvirus.eval.query.evalseason.RateeMappingModel;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class ReturnDraftCommandTest {

    private ReturnDraftCommandHandler handler;
    private Repository<PersonalEval> personalEvalRepository;

    @Before
    public void setUp() throws Exception {
        EvalSeasonMappingModelRepository mappingModelRepository = new EvalSeasonMappingModelRepositoryImpl();
        mappingModelRepository.add(
                new EvalSeasonMappingModel("eval2014", 0)
                        .updateMapping(
                                new RateeMappingModel(
                                        new UserModel("ratee1", "ratee1", ""),
                                        RateeType.MEMBER,
                                        new UserModel("first", "first", ""),
                                        new UserModel("second", "second", "")), 1L)
                        .updateMapping(
                                new RateeMappingModel(
                                        new UserModel("ratee2", "ratee2", ""),
                                        RateeType.MEMBER,
                                        new UserModel("first", "first", ""),
                                        new UserModel("second", "second", "")), 2L)
                        .updateMapping(
                                new RateeMappingModel(
                                        new UserModel("ratee3", "ratee3", ""),
                                        RateeType.MEMBER,
                                        new UserModel("first", "first", ""),
                                        new UserModel("second", "second", "")), 2L)
        );
        personalEvalRepository = mock(Repository.class);
        handler = new ReturnDraftCommandHandler(mappingModelRepository, personalEvalRepository);
    }

    public class GivenFirstEvalDoneAndSecondNotYetStarted {

        private PersonalEval eval1;
        private PersonalEval eval2;

        @Before
        public void setUp() {
            eval1 = PersonalEvalHelper.createPersonalEvalWithFirstDoneAndNoSecondEval("eval2014", "ratee1", "first");
            eval2 = PersonalEvalHelper.createPersonalEvalWithFirstDoneAndNoSecondEval("eval2014", "ratee2", "first");
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee1"))).thenReturn(eval1);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee2"))).thenReturn(eval2);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee3"))).thenThrow(new AggregateNotFoundException("", ""));
        }

        @Test
        public void whenReturnDraft_then_Should_Become_Draft() {
            handler.returnDraftOfFirstEval(new ReturnFirstEvalDraftCommand("eval2014", "first"));
            assertThat(eval1.isFirstTotalEvalDone(), equalTo(false));
            assertThat(eval2.isFirstTotalEvalDone(), equalTo(false));
        }

    }

    public class GivenSecondEvalStarted {
        private PersonalEval eval1;
        private PersonalEval eval2;
        private PersonalEval eval3;

        @Before
        public void setUp() {
            eval1 = PersonalEvalHelper.createPersonalEvalWithFirstDone("eval2014", "ratee1", "first", false);
            eval2 = PersonalEvalHelper.createPersonalEvalWithFirstDone("eval2014", "ratee2", "first", true);
            eval3 = PersonalEvalHelper.createPersonalEvalWithFirstDone("eval2014", "ratee3", "first", true);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee1"))).thenReturn(eval1);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee2"))).thenReturn(eval2);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee3"))).thenReturn(eval3);
        }

        @Test
        public void whenReturnDraft_then_Should_Be_FirstEvalDraft() {
            handler.returnDraftOfFirstEval(new ReturnFirstEvalDraftCommand("eval2014", "first"));
            assertThat(eval1.isFirstTotalEvalDone(), equalTo(false));
            assertThat(eval2.isFirstTotalEvalDone(), equalTo(false));
        }
    }

    public class GivenSecondEvalDone {
        private PersonalEval eval1;
        private PersonalEval eval2;
        private PersonalEval eval3;

        @Before
        public void setUp() {
            eval1 = PersonalEvalHelper.createPersonalEvalWithSecondDone("eval2014", "ratee1");
            eval2 = PersonalEvalHelper.createPersonalEvalWithSecondDone("eval2014", "ratee2");
            eval3 = PersonalEvalHelper.createPersonalEvalWithSecondDone("eval2014", "ratee3");
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee1"))).thenReturn(eval1);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee2"))).thenReturn(eval2);
            when(personalEvalRepository.load(PersonalEval.createId("eval2014", "ratee3"))).thenReturn(eval3);
        }

        @Test
        public void whenReturnDraft_then_Should_Throw_Exception() {
            try {
                handler.returnDraftOfFirstEval(new ReturnFirstEvalDraftCommand("eval2014", "first"));
                fail();
            } catch (AlreadySecondEvalDoneException e) {
            }
        }
    }

}
