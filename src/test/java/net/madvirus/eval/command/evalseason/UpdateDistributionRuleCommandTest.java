package net.madvirus.eval.command.evalseason;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.domain.evalseason.*;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateDistributionRuleCommandTest extends AbstractEvalSeasonCommandTest {

    public static final String EVAL_SEASON_ID = "eval-2014";
    public static final String FIRST_RATER = "firstRater";

    private Repository<PersonalEval> mockPersonalEvalRepository;

    @Before
    public void setUp() throws Exception {
        mockPersonalEvalRepository = mock(Repository.class);
        fixture.registerAnnotatedCommandHandler(new UpdateDistributionRuleCommandHandler(fixture.getRepository(), mockPersonalEvalRepository));
    }

    private UpdateDistributionRuleCommand createCommand() {
        return createCommand(FIRST_RATER);
    }

    private UpdateDistributionRuleCommand createCommand(String firstRaterId) {
        return new UpdateDistributionRuleCommand(
                EVAL_SEASON_ID, firstRaterId,
                Arrays.asList(
                        new DistributionRule("ruleName", 1, 1, 1, 1,
                                Arrays.asList("ratee11", "ratee12", "ratee13", "ratee14"))
                )
        );
    }

    @Test
    public void givenNoEvalSeason_thenThrowEx() throws Exception {
        fixture.given()
                .when(createCommand())
                .expectException(EvalSeasonNotFoundException.class);
    }

    public class GivenEvalSeason {

        private TestExecutor testExecutor;

        @Before
        public void setUp() {
            testExecutor = fixture.given(
                    new EvalSeasonCreatedEvent(EVAL_SEASON_ID, "평가", new Date()),
                    new MappingUpdatedEvent(EVAL_SEASON_ID,
                            Arrays.asList(
                                    new RateeMapping("ratee11", RateeType.MEMBER, FIRST_RATER, "secondRater"),
                                    new RateeMapping("ratee12", RateeType.MEMBER, FIRST_RATER, "secondRater"),
                                    new RateeMapping("ratee13", RateeType.MEMBER, FIRST_RATER, "secondRater"),
                                    new RateeMapping("ratee14", RateeType.MEMBER, FIRST_RATER, "secondRater")
                            ))
            );
        }

        @Test
        public void givenNoFirstRater_then_should_throw_ex() {
            testExecutor.when(createCommand("noFirstRater"))
                    .expectException(NotMatchingFirstRaterException.class);
        }


        @Test
        public void beforeFirstEvalDone_then_update_Rule() throws Exception {
            UpdateDistributionRuleCommand command = createCommand();

            EventCaptureMatcher matcher = new EventCaptureMatcher();
            testExecutor.when(command)
                    .expectEventsMatching(matcher);

            DistributionRuleUpdatedEvent event = (DistributionRuleUpdatedEvent) matcher.getPayload();
            assertThat(event.getFirstRaterId(), equalTo(command.getFirstRaterId()));
        }

        @Test
        public void when_command_has_IdBelongToManyRules_then_throw_Ex() throws Exception {
            UpdateDistributionRuleCommand command = new UpdateDistributionRuleCommand(
                    EVAL_SEASON_ID, FIRST_RATER,
                    Arrays.asList(
                            new DistributionRule("ruleName1", 1, 1, 0, 0,
                                    Arrays.asList("ratee11", "ratee12")),
                            new DistributionRule("ruleName2", 0, 1, 1, 1,
                                    Arrays.asList("ratee12", "ratee13", "ratee14"))
                    )
            );

            testExecutor.when(command)
                    .expectException(IdBelongToManyRulesException.class);
        }

        @Test
        public void when_command_has_GradeCountNotMatchRateesNumber_then_throw_Ex() throws Exception {
            UpdateDistributionRuleCommand command = new UpdateDistributionRuleCommand(
                    EVAL_SEASON_ID, FIRST_RATER,
                    Arrays.asList(
                            new DistributionRule("ruleName1", 1, 1, 1, 1,
                                    Arrays.asList("ratee11", "ratee12", "ratee13"))
                    )
            );

            testExecutor.when(command)
                    .expectException(BasGradeCountRuleException.class);
        }

        @Test
        public void givenFirstRaterHasEvalDone_then_throw_Ex() throws Exception {
            PersonalEval personalEval1 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVAL_SEASON_ID, "ratee11", false);
            PersonalEval personalEval2 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVAL_SEASON_ID, "ratee12", false);
            PersonalEval personalEval3 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVAL_SEASON_ID, "ratee13", false);
            PersonalEval personalEval4 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVAL_SEASON_ID, "ratee14", false);
            when(mockPersonalEvalRepository.load(any())).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    String id = (String) invocationOnMock.getArguments()[0];
                    if (PersonalEval.createId(EVAL_SEASON_ID, "ratee11").equals(id)) return personalEval1;
                    if (PersonalEval.createId(EVAL_SEASON_ID, "ratee12").equals(id)) return personalEval2;
                    if (PersonalEval.createId(EVAL_SEASON_ID, "ratee13").equals(id)) return personalEval3;
                    if (PersonalEval.createId(EVAL_SEASON_ID, "ratee14").equals(id)) return personalEval4;
                    throw new AggregateNotFoundException("not found id " + id, "");
                }
            });

            testExecutor.when(createCommand())
                    .expectException(FirstEvalDoneException.class);
        }

    }

}
