package net.madvirus.eval.api;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;

public class UpdateMappingCommand {
    @TargetAggregateIdentifier
    private String evalSeasonId;
    private List<RateeMapping> rateeMappings;

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public void setRateeMappings(List<RateeMapping> rateeMappings) {
        this.rateeMappings = rateeMappings;
    }

    public List<RateeMapping> getRateeMappings() {
        return rateeMappings;
    }

}
