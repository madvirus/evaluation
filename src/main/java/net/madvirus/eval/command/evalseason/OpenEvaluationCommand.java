package net.madvirus.eval.command.evalseason;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class OpenEvaluationCommand {
    @TargetAggregateIdentifier
    private String id;

    public OpenEvaluationCommand(String id) {
        this.id = id;
    }

    public OpenEvaluationCommand() {
    }

    public String getId() {
        return id;
    }

}
