package net.madvirus.eval.command.mappings;

import net.madvirus.eval.api.CreateMappingsCommand;
import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.MappingsCreatedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class CreateMappingsCommandTest {
    public static final String MAPPINGS_ID = "mappings-2014";
    public static final String EVALSEASON_ID = "eval-2014";

    private FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(Mappings.class);
        fixture.registerAnnotatedCommandHandler(new CreateMappingsCommandHandler(fixture.getRepository()));
    }

    @Test
    public void createNewMappingInfo() throws Exception {
        CreateMappingsCommand command = createCommand();
        fixture.given()
                .when(command)
                .expectEvents(new MappingsCreatedEvent(MAPPINGS_ID, EVALSEASON_ID));
    }

    private CreateMappingsCommand createCommand() {
        CreateMappingsCommand command = new CreateMappingsCommand();
        command.setId(MAPPINGS_ID);
        command.setEvalSeasonId(EVALSEASON_ID);
        return command;
    }

    @Test
    public void createDuplicateIdMappingInfo() throws Exception {
        CreateMappingsCommand command = new CreateMappingsCommand();
        command.setId(MAPPINGS_ID);
        fixture.given(new MappingsCreatedEvent(MAPPINGS_ID, EVALSEASON_ID))
                .when(command)
                .expectException(DuplicateIdException.class);
        command.setEvalSeasonId(EVALSEASON_ID);
    }

}
