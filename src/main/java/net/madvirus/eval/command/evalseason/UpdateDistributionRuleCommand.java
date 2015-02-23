package net.madvirus.eval.command.evalseason;

import net.madvirus.eval.domain.evalseason.DistributionRule;

import java.util.List;

public class UpdateDistributionRuleCommand {

    private String evalSeasonId;
    private String firstRaterId;
    private List<DistributionRule> rules;

    protected UpdateDistributionRuleCommand() {}

    public UpdateDistributionRuleCommand(String evalSeasonId, String firstRaterId, List<DistributionRule> rules) {
        this.evalSeasonId = evalSeasonId;
        this.firstRaterId = firstRaterId;
        this.rules = rules;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public List<DistributionRule> getRules() {
        return rules;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }
}
