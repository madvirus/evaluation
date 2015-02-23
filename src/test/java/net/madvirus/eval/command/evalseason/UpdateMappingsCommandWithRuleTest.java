package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.DistributionRuleUpdatedEvent;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.FirstEvalDoneException;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.command.EventCaptureMatcher;
import net.madvirus.eval.domain.evalseason.DistributionRule;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingEventListener;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepository;
import net.madvirus.eval.query.evalseason.EvalSeasonMappingModelRepositoryImpl;
import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.query.user.UserModelRepository;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateMappingsCommandWithRuleTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";

    private TestExecutor testExecutor;
    private UserModelRepository mockUserModelRepository;
    private Repository<PersonalEval> mockPersonalEvalRepository;

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
        AnnotationEventListenerAdapter eventListener = new AnnotationEventListenerAdapter(new EvalSeasonMappingEventListener(mappingModelRepository, mockUserModelRepository));
        fixture.getEventBus().subscribe(eventListener);

        fixture.registerAnnotatedCommandHandler(
                new UpdateMappingCommandHandler(
                        fixture.getRepository(),
                        mockPersonalEvalRepository,
                        mockUserModelRepository,
                        mappingModelRepository));

        List<Object> givenEvent = Arrays.asList(
                new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()),
                new MappingUpdatedEvent(EVALSEASON_ID,
                        Arrays.asList(
                                new RateeMapping("ratee11", RateeType.MEMBER, "first1", "second", "coll1"),
                                new RateeMapping("ratee12", RateeType.MEMBER, "first1", "second", "coll2"),
                                new RateeMapping("ratee13", RateeType.MEMBER, "first1", "second", "coll2"),
                                new RateeMapping("ratee14", RateeType.MEMBER, "first1", "second", "coll2"),
                                new RateeMapping("ratee21", RateeType.MEMBER, "first2", "second", "coll2"),
                                new RateeMapping("ratee22", RateeType.MEMBER, "first2", "second", "coll2"),
                                new RateeMapping("ratee31", RateeType.MEMBER, "first3", "second", "coll2"),
                                new RateeMapping("ratee32", RateeType.MEMBER, "first3", "second", "coll2")
                        )),
                new DistributionRuleUpdatedEvent(EVALSEASON_ID, "first1", Arrays.asList(
                        new DistributionRule("규칙1", 0, 0, 2, 0, Arrays.asList("ratee11", "ratee12")),
                        new DistributionRule("규칙2", 0, 1, 1, 0, Arrays.asList("ratee13", "ratee14"))
                )),
                new DistributionRuleUpdatedEvent(EVALSEASON_ID, "first3", Arrays.asList(
                        new DistributionRule("규칙1", 0, 0, 2, 0, Arrays.asList("ratee31", "ratee32"))
                ))
        );

        for (int i = 0 ; i < givenEvent.size() ; i++) {
            eventListener.handle(
                    new GenericDomainEventMessage<Object>(EVALSEASON_ID, i, givenEvent.get(i), null));
        }
        testExecutor = fixture.given(givenEvent);
    }

    @Test
    public void when_RateeInDistRule_change_FirstRater_then_ThatRule_Should_Be_Deleted() throws Exception {
        when(mockPersonalEvalRepository.load(any())).thenThrow(new AggregateNotFoundException("", ""));

        EventCaptureMatcher matcher = new EventCaptureMatcher();
        testExecutor.when(new UpdateMappingCommand(EVALSEASON_ID, Arrays.asList(new RateeMapping("ratee12", RateeType.MEMBER, "first2", "second", "coll2"))))
                .expectEventsMatching(matcher);
        List<Object> events = matcher.getPayloads();
        assertThat(events, hasSize(2));
        DistributionRuleUpdatedEvent ruleEvent = (DistributionRuleUpdatedEvent) events.get(0);
        assertThat(ruleEvent.getFirstRaterId(), equalTo("first1"));
        assertThat(ruleEvent.getRules(), hasSize(1));
        assertThat(ruleEvent.getRules().get(0).getName(), equalTo("규칙2"));
    }

}
