package net.madvirus.eval.api.evalseaon;

import net.madvirus.eval.domain.evalseason.DistributionRule;

import java.util.List;

public class DistributionRuleUpdatedEvent {
    private String evalSeasonId;
    private String firstRaterId;
    private List<DistributionRule> rules;

    protected DistributionRuleUpdatedEvent() {
    }

    public DistributionRuleUpdatedEvent(String evalSeasonId, String firstRaterId, List<DistributionRule> rules) {
        this.evalSeasonId = evalSeasonId;
        this.firstRaterId = firstRaterId;
        this.rules = rules;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public List<DistributionRule> getRules() {
        return rules;
    }
}
