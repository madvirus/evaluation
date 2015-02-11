package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.*;
import net.madvirus.eval.command.EventCaptureMatcher;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class StartColleagueEvalCommandTest extends AbstractEvalSeasonCommandTest {

    @Test
    public void given_StartedEvalSeason_then_should_be_ColleagueEval_Started() throws Exception {
        StartColleagueEvalCommand command = new StartColleagueEvalCommand("eval-2014");
        EventCaptureMatcher matcher = new EventCaptureMatcher();
        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가", new Date()), new EvaluationOpenedEvent("eval-2014"))
                .when(command)
                .expectEventsMatching(matcher);
        assertThat(matcher.getPayload(), instanceOf(ColleagueEvalStartedEvent.class));
    }

    @Test
    public void given_NotYetStartedEvalSeason_then_should_throw_ex() throws Exception {
        StartColleagueEvalCommand command = new StartColleagueEvalCommand("eval-2014");
        fixture.given(new EvalSeasonCreatedEvent("eval-2014", "평가", new Date()))
                .when(command)
                .expectException(EvalSeasonNotYetOpenedException.class);
    }
}
