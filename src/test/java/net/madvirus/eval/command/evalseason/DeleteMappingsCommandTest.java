package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.mock;

public class DeleteMappingsCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";
    protected UserModelRepository mockUserModelRepository;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockUserModelRepository = mock(UserModelRepository.class);
        fixture.registerAnnotatedCommandHandler(new UpdateMappingCommandHandler(fixture.getRepository(), mockUserModelRepository));
    }

    @Test
    public void notFound_throwEx() throws Exception {
        fixture.given()
                .when(createDeleteMappingsCommand())
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void deleteMappings() throws Exception {
        fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()),
                    new MappingUpdatedEvent(EVALSEASON_ID, Arrays.asList(
                            new RateeMapping("ratee1", RateeType.MEMBER, "rater1", "rater2", "colleague1"),
                            new RateeMapping("ratee2", RateeType.MEMBER, "rater1", "rater2", "colleague1")
                    )))
                .when(createDeleteMappingsCommand())
                .expectEvents(
                        new MappingDeletedEvent(EVALSEASON_ID, Arrays.asList("ratee1", "ratee2"))
                );
    }

    private DeleteMappingCommand createDeleteMappingsCommand() {
        return new DeleteMappingCommand(EVALSEASON_ID, Arrays.asList("ratee1", "ratee2"));
    }

}
