package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.DuplicateIdException;
import net.madvirus.eval.api.evalseaon.CreateEvalSeasonCommand;
import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import org.axonframework.domain.GenericDomainEventMessage;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.internal.matchers.CapturingMatcher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.axonframework.test.matchers.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CreateEvalSeasonCommandTest extends AbstractEvalSeasonCommandTest {
    public static final String EVALSEASON_ID = "eval-2014";

    @Test
    public void createNewEvalSeason() throws Exception {

        CaptureMatcher<EvalSeasonCreatedEvent> captureMatcher = new CaptureMatcher<>();

        fixture.given()
                .when(createCommand())
                .expectEventsMatching(captureMatcher);

        EvalSeasonCreatedEvent event = captureMatcher.getValue();
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

    private class CaptureMatcher<T> extends BaseMatcher<Iterable<T>> {
        private List<GenericDomainEventMessage> values = new ArrayList<>();

        @Override
        public boolean matches(Object o) {
            for (GenericDomainEventMessage t : (Iterable<GenericDomainEventMessage>)o) values.add(t);
            return true;
        }

        @Override
        public void describeTo(Description description) {
        }

        public T getValue() {
            return (T) values.get(0).getPayload();
        }
    }
}
