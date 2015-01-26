package net.madvirus.eval.api.evalseaon;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;

public class DeleteMappingCommand {
    @TargetAggregateIdentifier
    private String evalSeasonId;
    private List<String> rateeIds;

    public DeleteMappingCommand(String evalSeasonId, List<String> rateeIds) {
        this.evalSeasonId = evalSeasonId;
        this.rateeIds = rateeIds;
    }

    public DeleteMappingCommand() {
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public List<String> getRateeIds() {
        return rateeIds;
    }
}
