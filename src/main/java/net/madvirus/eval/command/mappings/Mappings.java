package net.madvirus.eval.command.mappings;

import net.madvirus.eval.api.MappingsCreatedEvent;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

public class Mappings extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;

    public Mappings() {
    }

    public Mappings(String id) {
        apply(new MappingsCreatedEvent(id, "eval-2014"));
    }

    @EventSourcingHandler
    protected void on(MappingsCreatedEvent event) {
        this.id = event.getMappingInfoId();
    }

}
