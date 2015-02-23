package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.api.RateeMapping;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;

public class UpdateMappingCommand {
    @TargetAggregateIdentifier
    private String evalSeasonId;
    private List<RateeMapping> rateeMappings;

    public UpdateMappingCommand(String evalSeasonId, List<RateeMapping> rateeMappings) {
        this.evalSeasonId = evalSeasonId;
        this.rateeMappings = rateeMappings;
    }

    public UpdateMappingCommand() {}

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public List<RateeMapping> getRateeMappings() {
        return rateeMappings;
    }

}
