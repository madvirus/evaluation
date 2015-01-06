package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.EvaluationOpenedEvent;
import net.madvirus.eval.api.OpenEvaluationCommand;
import org.junit.Test;

public class OpenEvalCommandTest extends AbstractEvalSeasonCommandTest {
    @Test
    public void open() throws Exception {
        OpenEvaluationCommand command = new OpenEvaluationCommand();
        command.setId("eval-2014");

        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가"))
                .when(command)
                .expectEvents(new EvaluationOpenedEvent("eval-2014"));
    }

    @Test
    public void whenAleadyOpend_throwEx() throws Exception {
        OpenEvaluationCommand command = new OpenEvaluationCommand();
        command.setId("eval-2014");

        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가"), new EvaluationOpenedEvent("eval-2014"))
                .when(command)
                .expectException(AleadyEvaluationOpenedException.class);
    }

}
