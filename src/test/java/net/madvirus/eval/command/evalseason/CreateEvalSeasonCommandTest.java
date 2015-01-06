package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.CreateEvalSeasonCommand;
import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.EvalSeasonCreatedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class CreateEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";

    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(EvalSeason.class);
        fixture.registerAnnotatedCommandHandler(new CreateEvalSeasonCommandHandler(fixture.getRepository()));
    }

    @Test
    public void createNewEvalSeason() throws Exception {
        fixture.given()
                .when(createCommand())
                .expectEvents(new EvalSeasonCreatedEvent(EVALSEASON_ID, "2014 평가"));
    }

    private CreateEvalSeasonCommand createCommand() {
        CreateEvalSeasonCommand command = new CreateEvalSeasonCommand();
        command.setEvalSeasonId(EVALSEASON_ID);
        command.setName("2014 평가");
        return command;
    }

    @Test
    public void duplicateId() {
        fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가"))
                .when(createCommand())
                .expectException(DuplicateIdException.class);
    }
}
