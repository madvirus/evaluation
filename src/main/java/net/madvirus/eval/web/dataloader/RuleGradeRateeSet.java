package net.madvirus.eval.web.dataloader;

import java.util.List;

public class RuleGradeRateeSet {
    private final RuleData ruleData;
    private final List<FirstTotalEvalSummary> summaryList;

    public RuleGradeRateeSet(RuleData ruleData, List<FirstTotalEvalSummary> summaryList) {
        this.ruleData = ruleData;
        this.summaryList = summaryList;
    }

    public RuleData getRuleData() {
        return ruleData;
    }

    public List<FirstTotalEvalSummary> getSummaryList() {
        return summaryList;
    }
}
