package net.madvirus.eval.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class OpenEvaluationCommand {
    @TargetAggregateIdentifier
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
