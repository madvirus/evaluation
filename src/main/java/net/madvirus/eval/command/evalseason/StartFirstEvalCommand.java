package net.madvirus.eval.command.evalseason;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class StartFirstEvalCommand {
    @TargetAggregateIdentifier
    private String evalSeasonId;

    public StartFirstEvalCommand(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }
}
