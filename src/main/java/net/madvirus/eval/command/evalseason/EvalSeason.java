package net.madvirus.eval.command.evalseason;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.evalseaon.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.commandhandling.annotation.CommandHandlingMember;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcedMember;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties("eventContainer")
public class EvalSeason extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;
    private String name;
    private boolean opened;
    private Date creationDate;
    private boolean colleagueEvalutionStarted;

    private Mappings mappings;

    protected EvalSeason() {
    }

    public EvalSeason(String id, String name) {
        apply(new EvalSeasonCreatedEvent(id, name, new Date()));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOpened() {
        return opened;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isColleagueEvalutionStarted() {
        return colleagueEvalutionStarted;
    }

    @EventSourcingHandler
    protected void on(EvalSeasonCreatedEvent event) {
        this.id = event.getEvalSeasonId();
        this.name = event.getName();
        this.opened = false;
        this.creationDate = event.getCreationDate();
        this.mappings = new Mappings(this.id);
    }

    public void updateMapping(UpdateMappingCommand command) {
        MappingUpdatedEvent mappingUpdatedEvent = mappings.updateMapping(command);
        apply(mappingUpdatedEvent);
    }

    @EventSourcingHandler
    protected void on(MappingUpdatedEvent event) {
        mappings.on(event);
    }

    @CommandHandler
    public void open(OpenEvaluationCommand command) {
        if (opened) throw new AleadyEvaluationOpenedException();
        apply(new EvaluationOpenedEvent(id));
    }

    @EventSourcingHandler
    protected void on(EvaluationOpenedEvent event) {
        this.opened = true;
    }

    @CommandHandler
    public void deleteMapping(DeleteMappingCommand command) {
        Optional<MappingDeletedEvent> eventOp = mappings.deleteMapping(command);
        eventOp.ifPresent(event -> apply(event));
    }

    @EventSourcingHandler
    protected void on(MappingDeletedEvent event) {
        mappings.on(event);
    }
}
