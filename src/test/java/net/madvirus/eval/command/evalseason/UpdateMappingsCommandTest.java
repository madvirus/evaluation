package net.madvirus.eval.command.evalseason;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.api.personaleval.colleague.ColleagueCompetencyEvaluatedEvent;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.FirstEvalStartedException;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.domain.personaleval.SecondEvalStartedException;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingEventListener;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepositoryImpl;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.testhelper.EventHelper;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static net.madvirus.eval.testhelper.CompetencyHelper.createCompetencyEvalSet;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class UpdateMappingsCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";
    public static final String RATEE_11 = "ratee11";
    public static final String RATEE_12 = "ratee12";
    public static final String RATEE_21 = "ratee21";
    public static final String RATEE_22 = "ratee22";
    public static final String COLLEAGUE_1 = "colleague1";
    public static final String COLLEAGUE_2 = "colleague2";
    public static final String FIRST_RATER_1 = "firstRater1";
    public static final String FIRST_RATER_2 = "firstRater2";
    public static final String SECOND_RATER_1 = "secondRater";
    public static final String SECOND_RATER_2 = "secondRater1";

    private Repository<PersonalEval> mockPersonalEvalRepository;
    protected UserModelRepository mockUserModelRepository;
    private AnnotationEventListenerAdapter eventListener;

    @Before
    public void setUp() throws Exception {
        mockUserModelRepository = mock(UserModelRepository.class);
        when(mockUserModelRepository.findOne(any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String id = (String) invocationOnMock.getArguments()[0];
                return new UserModel(id, id, "", 1L);
            }
        });

        mockPersonalEvalRepository = mock(Repository.class);

        EvalSeasonMappingModelRepository mappingModelRepository = new EvalSeasonMappingModelRepositoryImpl();
        eventListener = new AnnotationEventListenerAdapter(new EvalSeasonMappingEventListener(mappingModelRepository, mockUserModelRepository));
        fixture.getEventBus().subscribe(eventListener);

        fixture.registerAnnotatedCommandHandler(
                new UpdateMappingCommandHandler(
                        fixture.getRepository(),
                        mockPersonalEvalRepository,
                        mockUserModelRepository,
                        mappingModelRepository));
    }

    @Test
    public void givenNoEvalSeason_should_throw_Ex() throws Exception {
        fixture.given()
                .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                        new RateeMapping(RATEE_11, RateeType.TEAM_LEADER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2),
                        new RateeMapping(RATEE_12, RateeType.TEAM_LEADER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1))))
                .expectException(EvalSeasonNotFoundException.class);
    }

    public class GivenEvalSeasonAndNoPersonalEval {

        private TestExecutor testExecutor;

        @Before
        public void setUp() throws Exception {
            testExecutor = fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()));
        }

        @Test
        public void when_command_UpdateMapping_then_Mapping_should_be_Updated() throws Exception {
            when(mockPersonalEvalRepository.load(any())).thenThrow(new AggregateNotFoundException("", ""));
            testExecutor
                    .when(
                            new UpdateMappingCommand(EVALSEASON_ID,
                                    Arrays.asList(
                                            new RateeMapping(RATEE_11, RateeType.TEAM_LEADER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2),
                                            new RateeMapping(RATEE_12, RateeType.TEAM_LEADER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1))))
                    .expectEvents(
                            new MappingUpdatedEvent(EVALSEASON_ID,
                                    Arrays.asList(
                                            new RateeMapping(RATEE_11, RateeType.TEAM_LEADER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2),
                                            new RateeMapping(RATEE_12, RateeType.TEAM_LEADER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1)))
                    );
        }

    }


    public class GivenEvalSeasonWithMappingAndPersonalEval {
        private PersonalEval personalEval1;
        private TestExecutor testExecutor;

        @Before
        public void setUp() {
            List<Object> givenEvent = Arrays.asList(
                    new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()),
                    new MappingUpdatedEvent(EVALSEASON_ID,
                            Arrays.asList(
                                    new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1),
                                    new RateeMapping(RATEE_12, RateeType.MEMBER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1),
                                    new RateeMapping(RATEE_21, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_1, COLLEAGUE_1),
                                    new RateeMapping(RATEE_22, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_1, COLLEAGUE_1)
                            ))
            );
            for (int i = 0 ; i < givenEvent.size() ; i++) {
                eventListener.handle(
                        new GenericDomainEventMessage<Object>(EVALSEASON_ID, i, givenEvent.get(i), null));
            }
            testExecutor = fixture.given(givenEvent);

            personalEval1 = PersonalEvalHelper.createPersonalEvalWithSelfDone(EVALSEASON_ID, RATEE_11, FIRST_RATER_1, SECOND_RATER_1, false, false);
            when(mockPersonalEvalRepository.load(any())).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    String id = (String) invocationOnMock.getArguments()[0];
                    if (id.equals(PersonalEval.createId(EVALSEASON_ID, RATEE_11))) return personalEval1;
                    throw new AggregateNotFoundException(id, "");
                }
            });
        }

        @Test
        public void givenSelfDonePersonalEval_then_mapping_should_be_applied() throws Exception {
            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID,
                            Arrays.asList(
                                    new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2),
                                    new RateeMapping(RATEE_12, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1))))
                    .expectEvents(
                            new MappingUpdatedEvent(EVALSEASON_ID,
                                    Arrays.asList(
                                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2),
                                            new RateeMapping(RATEE_12, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1)))
                    );
        }

        @Test
        public void beforeFirstRaterStart_when_Change_FirstRater_then_mapping_should_be_applied() throws Exception {
            EventCaptureMatcher matcher = new EventCaptureMatcher();
            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2)
                    )))
                    .expectEventsMatching(matcher);

            assertThat(personalEval1.checkFirstRater(FIRST_RATER_1), equalTo(false));
            assertThat(personalEval1.checkFirstRater(FIRST_RATER_2), equalTo(true));
        }

        @Test
        public void afterFirstRaterStart_when_Change_FirstRater_then_should_throw_Ex() throws Exception {
            personalEval1.on(EventHelper.firstPerfEvaluatedEvent(EVALSEASON_ID, RATEE_11));

            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_1, COLLEAGUE_1, COLLEAGUE_2)
                    )))
                    .expectException(FirstEvalStartedException.class);

            assertThat(personalEval1.checkFirstRater(FIRST_RATER_1), equalTo(true));
        }

        @Test
        public void beforeSecondRaterStart_when_Change_SecondRater_then_mapping_should_be_applied() throws Exception {
            EventCaptureMatcher matcher = new EventCaptureMatcher();
            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, SECOND_RATER_2, COLLEAGUE_1, COLLEAGUE_2)
                    )))
                    .expectEventsMatching(matcher);

            assertThat(personalEval1.checkSecondRater(SECOND_RATER_1), equalTo(false));
            assertThat(personalEval1.checkSecondRater(SECOND_RATER_2), equalTo(true));
        }

        @Test
        public void afterSecondRaterStart_when_Change_FirstRater_then_should_throw_Ex() throws Exception {
            personalEval1.on(EventHelper.secondPerfEvaluatedEvent(EVALSEASON_ID, RATEE_11));

            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1, COLLEAGUE_2)
                    )))
                    .expectException(SecondEvalStartedException.class);

            assertThat(personalEval1.checkSecondRater(SECOND_RATER_1), equalTo(true));
        }


        @Test
        public void givenAlreadyFirstEvalDone_should_throw_ex() {
            personalEval1.on(EventHelper.firstPerfEvaluatedEvent(EVALSEASON_ID, RATEE_11));
            personalEval1.on(EventHelper.firstCompeEvaluatedEvent(EVALSEASON_ID, RATEE_11, FIRST_RATER_1));
            personalEval1.on(EventHelper.firstTotalEvaluatedEvent(EVALSEASON_ID, true));

            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, COLLEAGUE_2),
                            new RateeMapping(RATEE_12, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1)
                    )))
                    .expectException(FirstEvalDoneException.class);
        }

        @Test
        public void givenAlreadyColleagueEvalDone_should_throw_Ex() throws Exception {
            personalEval1.on(new ColleagueCompetencyEvaluatedEvent(
                    PersonalEval.createId(EVALSEASON_ID, RATEE_11),
                    COLLEAGUE_1,
                    createCompetencyEvalSet(COLLEAGUE_1, false, false, true)
            ));

            testExecutor
                    .when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_1, COLLEAGUE_2),
                            new RateeMapping(RATEE_12, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_2, COLLEAGUE_1)
                    )))
                    .expectException(SomeColleagueEvalDoneException.class);
        }

        @Test
        public void when_newFirstRater_has_already_done_firstEval_then_Should_Throw_Ex() throws Exception {
            PersonalEval eval21 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVALSEASON_ID, RATEE_21, FIRST_RATER_2, false);
            PersonalEval eval22 = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVALSEASON_ID, RATEE_22, FIRST_RATER_2, false);
            Mockito.reset(mockPersonalEvalRepository);

            when(mockPersonalEvalRepository.load(any())).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    String id = (String) invocationOnMock.getArguments()[0];
                    if (id.equals(PersonalEval.createId(EVALSEASON_ID, RATEE_21))) return eval21;
                    if (id.equals(PersonalEval.createId(EVALSEASON_ID, RATEE_22))) return eval22;
                    throw new AggregateNotFoundException(id, "");
                }
            });

            testExecutor.when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(
                    new RateeMapping(RATEE_11, RateeType.MEMBER, FIRST_RATER_2, SECOND_RATER_1, COLLEAGUE_1))))
                    .expectException(FirstEvalDoneException.class);
        }

    }

}
