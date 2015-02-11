package net.madvirus.eval.api.evalseaon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.madvirus.eval.api.RateeMapping;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.Date;
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

    public EvalSeason() {
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

    public boolean containsRatee(String rateeId) {
        return mappings.containsRatee(rateeId);
    }

    public RateeMapping getRateeMapping(String userId) {
        return mappings.getRateeMapping(userId);
    }

    @EventSourcingHandler
    public void on(EvalSeasonCreatedEvent event) {
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
    public void on(MappingUpdatedEvent event) {
        mappings.on(event);
    }

    @CommandHandler
    public void handle(OpenEvaluationCommand command) {
        if (opened) throw new AlreadyEvaluationOpenedException();
        apply(new EvaluationOpenedEvent(id));
    }

    @EventSourcingHandler
    public void on(EvaluationOpenedEvent event) {
        this.opened = true;
    }

    @CommandHandler
    public void handle(DeleteMappingCommand command) {
        Optional<MappingDeletedEvent> eventOp = mappings.deleteMapping(command);
        eventOp.ifPresent(event -> apply(event));
    }

    @EventSourcingHandler
    public void on(MappingDeletedEvent event) {
        mappings.on(event);
    }

    @CommandHandler
    public void handle(StartColleagueEvalCommand command) {
        if (!opened) throw new EvalSeasonNotYetOpenedException();
        if (colleagueEvalutionStarted) throw new ColleagueEvalAlreadyStartedException();
        apply(new ColleagueEvalStartedEvent(this.id));
    }

    @EventSourcingHandler
    public void on(ColleagueEvalStartedEvent event) {
        colleagueEvalutionStarted = true;
    }

    public boolean containsColleagueRater(String rateeId, String raterId) {
        return mappings.containsColleagueRater(rateeId, raterId);
    }
}
