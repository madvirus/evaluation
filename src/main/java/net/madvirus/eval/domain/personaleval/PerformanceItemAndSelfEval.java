package net.madvirus.eval.domain.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceItemAndSelfEval {
    @JsonProperty
    private PerformanceItem item;
    @JsonProperty
    private ItemEval eval;

    protected PerformanceItemAndSelfEval() {}

    public PerformanceItemAndSelfEval(PerformanceItem item, ItemEval eval) {
        this.item = item;
        this.eval = eval;
    }

    public PerformanceItem getItem() {
        return item;
    }

    public ItemEval getEval() {
        return eval;
    }
}
