package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.DeleteMappingCommand;
import net.madvirus.eval.api.evalseaon.MappingDeletedEvent;
import net.madvirus.eval.api.evalseaon.MappingUpdatedEvent;
import net.madvirus.eval.api.evalseaon.UpdateMappingCommand;
import org.apache.commons.collections.map.HashedMap;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mappings extends AbstractAnnotatedEntity {
    private Map<String, RateeMapping> mappings = new HashedMap();

    private EvalSeason parent;

    public Mappings(EvalSeason parent) {
        this.parent = parent;
    }

    public void updateMapping(UpdateMappingCommand command) {
        apply(new MappingUpdatedEvent(parent.getId(), command.getRateeMappings()));
    }

    @EventSourcingHandler
    protected void on(MappingUpdatedEvent event) {
        event.getMappings().forEach(mapping -> mappings.put(mapping.getRateeId(), mapping));
    }

    @CommandHandler
    public void deleteMapping(DeleteMappingCommand command) {
        List<String> deletedIds = new ArrayList<>();
        command.getRateeIds().forEach(rateeId -> {
            if (mappings.containsKey(rateeId)) {
                deletedIds.add(rateeId);
            }
        });
        if (!deletedIds.isEmpty()) {
            apply(new MappingDeletedEvent(parent.getId(), deletedIds));
        }
    }

    @EventSourcingHandler
    protected void on(MappingDeletedEvent event) {
        event.getDeletedRateeIds().forEach(rateeId -> mappings.remove(rateeId));
    }

}
