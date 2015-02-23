package net.madvirus.eval.command.evalseason;

import net.avh4.test.junit.Nested;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.testhelper.EventHelper;
import net.madvirus.eval.testhelper.PersonalEvalHelper;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.axonframework.test.TestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Nested.class)
public class DeleteMappingsCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";
    public static final String RATEE1 = "ratee1";
    public static final String RATEE2 = "ratee2";
    private Repository<PersonalEval> mockPersonalEvalRepository;

    @Before
    public void setUp() {
        mockPersonalEvalRepository = mock(Repository.class);
        fixture.registerAnnotatedCommandHandler(
                new DeleteMappingCommandHandler(fixture.getRepository(), mockPersonalEvalRepository));
    }

    @Test
    public void givenNoEvalSeason_then_Throw_Ex() throws Exception {
        fixture.given()
                .when(createDeleteMappingsCommand(new String[]{RATEE1, RATEE2}))
                .expectException(EvalSeasonNotFoundException.class);
    }

    public class GivenEvalSeason {

        private TestExecutor testExecutor;

        @Before
        public void setUp() {
            testExecutor = fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()),
                    new MappingUpdatedEvent(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping(RATEE1, RateeType.MEMBER, "rater1", "rater2", "colleague1"),
                            new RateeMapping(RATEE2, RateeType.MEMBER, "rater1", "rater2", "colleague1")
                    )));
        }

        @Test
        public void given_NoPersonalEval_then_Mapping_should_be_deleted() throws Exception {
            when(mockPersonalEvalRepository.load(any())).thenThrow(new AggregateNotFoundException("", ""));
            testExecutor
                    .when(createDeleteMappingsCommand(RATEE1, RATEE2))
                    .expectEvents(
                            new MappingDeletedEvent(EVALSEASON_ID, Arrays.asList(RATEE1, RATEE2))
                    );
        }

        public class GivenPersonalEval {

            @Test
            public void mapping_should_be_deleted_And_delete_PersonalEval() throws Exception {
                PersonalEval personalEval = PersonalEvalHelper.createPersonalEvalWithSelfDone(EVALSEASON_ID, RATEE1, false, false);
                when(mockPersonalEvalRepository.load(PersonalEval.createId(EVALSEASON_ID, RATEE1)))
                        .thenReturn(personalEval);

                testExecutor
                        .when(createDeleteMappingsCommand(RATEE1))
                        .expectEvents(
                                new MappingDeletedEvent(EVALSEASON_ID, Arrays.asList(RATEE1))
                        );

                assertThat(personalEval.isDeleted(), equalTo(true));
            }


            @Test
            public void given_FirstEvalDone_then_should_throw_Ex() throws Exception {
                PersonalEval personalEval = PersonalEvalHelper.createPersonalEvalWithFirstDone(EVALSEASON_ID, RATEE1, false);
                when(mockPersonalEvalRepository.load(PersonalEval.createId(EVALSEASON_ID, RATEE1)))
                        .thenReturn(personalEval);

                testExecutor
                        .when(createDeleteMappingsCommand(RATEE1))
                        .expectException(CanNotDeleteMappingException.class);
            }

        }

    }

    private DeleteMappingCommand createDeleteMappingsCommand(String... rateeIds) {
        return new DeleteMappingCommand(EVALSEASON_ID, Arrays.asList(rateeIds));
    }

}
