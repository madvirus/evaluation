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
import java.util.Optional;

public class Mappings { //extends AbstractAnnotatedEntity {
    private Map<String, RateeMapping> mappings = new HashedMap();

    private String parentId;

    protected Mappings() {
    }

    public Mappings(String parentId) {
        this.parentId = parentId;
    }

    public MappingUpdatedEvent updateMapping(UpdateMappingCommand command) {
        return new MappingUpdatedEvent(parentId, command.getRateeMappings());
    }

    protected void on(MappingUpdatedEvent event) {
        event.getMappings().forEach(mapping -> mappings.put(mapping.getRateeId(), mapping));
    }

    public Optional<MappingDeletedEvent> deleteMapping(DeleteMappingCommand command) {
        List<String> deletedIds = new ArrayList<>();
        command.getRateeIds().forEach(rateeId -> {
            if (mappings.containsKey(rateeId)) {
                deletedIds.add(rateeId);
            }
        });
        return deletedIds.isEmpty() ?
                Optional.empty() :
                Optional.of(new MappingDeletedEvent(parentId, deletedIds));
    }

    protected void on(MappingDeletedEvent event) {
        event.getDeletedRateeIds().forEach(rateeId -> mappings.remove(rateeId));
    }

}
