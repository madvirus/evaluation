package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.evalseaon.EvalSeasonCreatedEvent;
import net.madvirus.eval.api.evalseaon.EvaluationOpenedEvent;
import net.madvirus.eval.api.evalseaon.OpenEvaluationCommand;
import net.madvirus.eval.api.evalseaon.UpdateMappingCommand;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.commandhandling.annotation.CommandHandlingMember;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcedMember;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import java.util.Date;


public class EvalSeason extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;
    private String name;
    private boolean open;
    private Date creationDate;

    @CommandHandlingMember
    @EventSourcedMember
    private Mappings mappings;

    public EvalSeason() {
    }

    public EvalSeason(String id, String name) {
        apply(new EvalSeasonCreatedEvent(id, name, new Date()));
    }

    public String getId() {
        return id;
    }

    @EventSourcingHandler
    protected void on(EvalSeasonCreatedEvent event) {
        this.id = event.getEvalSeasonId();
        this.name = event.getName();
        this.open = false;
        this.creationDate = event.getCreationDate();
        this.mappings = new Mappings(this);
    }

    public void updateMapping(UpdateMappingCommand command) {
        mappings.updateMapping(command);
    }

    @CommandHandler
    public void open(OpenEvaluationCommand command) {
        if (open) throw new AleadyEvaluationOpenedException();
        apply(new EvaluationOpenedEvent(id));
    }

    @EventSourcingHandler
    protected void on(EvaluationOpenedEvent event) {
        this.open = true;
    }
}
