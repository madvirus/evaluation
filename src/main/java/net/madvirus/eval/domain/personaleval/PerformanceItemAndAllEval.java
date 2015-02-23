package net.madvirus.eval.domain.personaleval;

public class PerformanceItemAndAllEval {
    private PerformanceItem item;
    private ItemEval selfEval;
    private ItemEval firstEval;
    private ItemEval secondEval;

    public PerformanceItemAndAllEval(PerformanceItem item, ItemEval selfEval, ItemEval firstEval, ItemEval secondEval) {
        this.item = item;
        this.selfEval = selfEval;
        this.firstEval = firstEval;
        this.secondEval = secondEval;
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

    public ItemEval getSecondEval() {
        return secondEval;
    }
}
