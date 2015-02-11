package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceItemAndAllEval {
    @JsonProperty
    private PerformanceItem item;
    @JsonProperty
    private ItemEval selfEval;

    @JsonProperty
    private ItemEval firstEval;

    public PerformanceItemAndAllEval(PerformanceItem item, ItemEval selfEval, ItemEval firstEval) {
        this.item = item;
        this.selfEval = selfEval;
        this.firstEval = firstEval;
    }

    public PerformanceItem getItem() {
        return item;
    }

    public ItemEval getSelfEval() {
        return selfEval;
    }

    public ItemEval getFirstEval() {
        return firstEval;
    }
}
