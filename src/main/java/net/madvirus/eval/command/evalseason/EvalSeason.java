package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.EvalSeasonCreatedEvent;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcedMember;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

public class EvalSeason extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;
    private String name;
    private boolean open;

    @EventSourcedMember
    private Mappings mappings;

    public EvalSeason() {
    }

    public EvalSeason(String id, String name) {
        apply(new EvalSeasonCreatedEvent(id, name));
    }

    @EventSourcingHandler
    protected void on(EvalSeasonCreatedEvent event) {
        this.id = event.getEvalSeasonId();
        this.name = event.getName();
        this.open = false;
        this.mappings = new Mappings();
    }
}
