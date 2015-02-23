package net.madvirus.eval.api.personaleval.second;

import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;

public class SecondPerformanceEvaluatedEvent {
    private String personalEvalId;
    private List<ItemEval> itemEvals;
    private ItemEval totalEval;

    protected SecondPerformanceEvaluatedEvent() {}

    public SecondPerformanceEvaluatedEvent(String personalEvalId, List<ItemEval> itemEvals, ItemEval totalEval) {
        this.personalEvalId = personalEvalId;
        this.itemEvals = itemEvals;
        this.totalEval = totalEval;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public ItemEval getTotalEval() {
        return totalEval;
    }

    public List<ItemEval> getItemEvals() {
        return itemEvals;
    }
}
