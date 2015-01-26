package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.EvaluationOpenedEvent;
import net.madvirus.eval.api.evalseaon.OpenEvaluationCommand;
import org.junit.Test;

import java.util.Date;

public class OpenEvalCommandTest extends AbstractEvalSeasonCommandTest {
    @Test
    public void open() throws Exception {
        OpenEvaluationCommand command = new OpenEvaluationCommand("eval-2014");

        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가", new Date()))
                .when(command)
                .expectEvents(new EvaluationOpenedEvent("eval-2014"));
    }

    @Test
    public void whenAleadyOpend_throwEx() throws Exception {
        OpenEvaluationCommand command = new OpenEvaluationCommand("eval-2014");

        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가", new Date()), new EvaluationOpenedEvent("eval-2014"))
                .when(command)
                .expectException(AleadyEvaluationOpenedException.class);
    }

}
