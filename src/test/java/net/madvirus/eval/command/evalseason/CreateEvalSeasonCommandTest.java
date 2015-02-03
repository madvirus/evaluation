package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.command.EventCaptureMatcher;
import org.junit.Test;

import java.util.Date;

import static org.axonframework.test.matchers.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CreateEvalSeasonCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";

    @Test
    public void createNewEvalSeason() throws Exception {

        EventCaptureMatcher captureMatcher = new EventCaptureMatcher();

        fixture.given()
                .when(createCommand())
                .expectEventsMatching(captureMatcher);

        EvalSeasonCreatedEvent event = (EvalSeasonCreatedEvent) captureMatcher.getValue().getPayload();
        assertThat(event.getEvalSeasonId(), equalTo(EVALSEASON_ID));
        assertThat(event.getName(), equalTo("2014 평가"));
    }

    private CreateEvalSeasonCommand createCommand() {
        CreateEvalSeasonCommand command = new CreateEvalSeasonCommand(EVALSEASON_ID, "2014 평가");
        return command;
    }

    @Test
    public void duplicateId() {
        fixture.given(new EvalSeasonCreatedEvent(EVALSEASON_ID, "평가", new Date()))
                .when(createCommand())
                .expectException(DuplicateIdException.class);
    }

}
