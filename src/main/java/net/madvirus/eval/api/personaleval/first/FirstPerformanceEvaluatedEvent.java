package net.madvirus.eval.api.personaleval.first;

import net.madvirus.eval.api.personaleval.ItemEval;

import java.util.List;

public class FirstPerformanceEvaluatedEvent {
    private String personalEvalId;
    private List<ItemEval> itemEvals;
    private ItemEval totalEval;

    protected FirstPerformanceEvaluatedEvent() {}

    public FirstPerformanceEvaluatedEvent(String personalEvalId, List<ItemEval> itemEvals, ItemEval totalEval) {
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
