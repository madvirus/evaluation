package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.MappingUpdatedEvent;
import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.UpdateMappingCommand;
import org.apache.commons.collections.map.HashedMap;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.Map;

public class Mappings extends AbstractAnnotatedEntity {
    private Map<String, RateeMapping> mappings = new HashedMap();

    private EvalSeason parent;

    public Mappings(EvalSeason parent) {
        this.parent = parent;
    }

    @CommandHandler
    public void handle(UpdateMappingCommand command) {
        command.getRateeMappings().forEach(mapping -> {
            apply(new MappingUpdatedEvent(parent.getId(), mapping));
        });
    }

    @EventSourcingHandler
    protected void on(MappingUpdatedEvent event) {
        mappings.put(event.getMapping().getRateeId(), event.getMapping());
    }
}
