package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.query.user.UserModelRepository;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class AbstractEvalSeasonCommandTest {
    protected FixtureConfiguration fixture;
    protected UserModelRepository mockUserModelRepository;

    @Before
    public void setUp() throws Exception {
        mockUserModelRepository = mock(UserModelRepository.class);
        fixture = Fixtures.newGivenWhenThenFixture(EvalSeason.class);
        fixture.registerAnnotatedCommandHandler(new AggregateAnnotationCommandHandler(EvalSeason.class, fixture.getRepository()));
        fixture.registerAnnotatedCommandHandler(new CreateEvalSeasonCommandHandler(fixture.getRepository()));
        fixture.registerAnnotatedCommandHandler(new UpdateMappingCommandHandler(fixture.getRepository(), mockUserModelRepository));
    }
}
