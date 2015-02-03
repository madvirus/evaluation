package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.personaleval.CreatePersonalEvalCommand;
import net.madvirus.eval.api.personaleval.PersonalEvaluationCreatedEvent;
import net.madvirus.eval.command.EventCaptureMatcher;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CreatePersonalEvalCommandTest {
    protected FixtureConfiguration fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(PersonalEval.class);
        fixture.registerAnnotatedCommandHandler(new CreatePersonalEvalCommandHandler(fixture.getRepository()));
    }

    @Test
    public void whenSendCreateCommand_thenShouldPublished_CreatedEvent() throws Exception {
        EventCaptureMatcher captureMatcher = new EventCaptureMatcher();
        fixture.given()
                .when(createCommand())
                .expectEventsMatching(captureMatcher);

        PersonalEvaluationCreatedEvent event = (PersonalEvaluationCreatedEvent) captureMatcher.getPayload();
        assertThat(event.getPersonalEvalId(), equalTo("EVAL1024-bkchoi"));
    }

    private CreatePersonalEvalCommand createCommand() {
        return new CreatePersonalEvalCommand("EVAL1024", "bkchoi");
    }

    @Test
    public void givenAleadyPersonalEvalExists_WhenSendCreateCommand_thenShouldThrowException() throws Exception {
        fixture.given(new PersonalEvaluationCreatedEvent("EVAL1024-bkchoi", "EVAL1024", "bkchoi"))
                .when(createCommand())
                .expectException(DuplicateIdException.class);
    }
}
