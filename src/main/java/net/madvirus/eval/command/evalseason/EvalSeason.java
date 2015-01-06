package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.EvaluationOpenedEvent;
import net.madvirus.eval.api.OpenEvaluationCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.commandhandling.annotation.CommandHandlingMember;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcedMember;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

public class EvalSeason extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;
    private String name;
    private boolean open;

    @CommandHandlingMember
    @EventSourcedMember
    private Mappings mappings;

    public EvalSeason() {
    }

    public EvalSeason(String id, String name) {
        apply(new EvalSeasonCreatedEvent(id, name));
    }

    public String getId() {
        return id;
    }

    @CommandHandler
    public void open(OpenEvaluationCommand command) {
        if (open) throw new AleadyEvaluationOpenedException();
        apply(new EvaluationOpenedEvent(id));
    }

    @EventSourcingHandler
    protected void on(EvalSeasonCreatedEvent event) {
        this.id = event.getEvalSeasonId();
        this.name = event.getName();
        this.open = false;
        this.mappings = new Mappings(this);
    }

    @EventSourcingHandler
    protected void on(EvaluationOpenedEvent event) {
        this.open = true;
    }
}
