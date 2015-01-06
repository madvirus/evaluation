package net.madvirus.eval.command.evalseason;

import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;

public class AbstractEvalSeasonCommandTest {
    protected FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(EvalSeason.class);
        fixture.registerAnnotatedCommandHandler(new AggregateAnnotationCommandHandler(EvalSeason.class, fixture.getRepository()));
        fixture.registerAnnotatedCommandHandler(new CreateEvalSeasonCommandHandler(fixture.getRepository()));
    }
}
