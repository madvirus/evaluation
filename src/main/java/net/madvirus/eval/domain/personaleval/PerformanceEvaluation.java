package net.madvirus.eval.domain.personaleval;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PerformanceEvaluation {
    private List<PerformanceItem> items;

    private Optional<PerformanceEvalSet> selfEvals;
    private Optional<PerformanceEvalSet> firstEvalSet;
    private Optional<PerformanceEvalSet> secondEvalSet;

    public PerformanceEvaluation() {
        items = new ArrayList<>();
        selfEvals = Optional.empty();
        firstEvalSet = Optional.empty();
        secondEvalSet = Optional.empty();
    }

    public List<PerformanceItem> getItems() {
        return items;
    }

    public Optional<PerformanceEvalSet> getSelfEvalSet() {
        return selfEvals;
    }

    public void updateSelfPerfEval(List<PerformanceItemAndSelfEval> perfItemAndSelfEvals, boolean done) {
        items.clear();
        List<ItemEval> selfItemEvals = new ArrayList<>();
        perfItemAndSelfEvals.forEach(ie -> {
            items.add(ie.getItem());
            selfItemEvals.add(ie.getEval());
        });
        selfEvals = Optional.of(new PerformanceEvalSet(selfItemEvals, null, done));
    }

    public boolean isSelfEvalDone() {
        return selfEvals.flatMap(eval -> Optional.of(eval.isDone())).orElse(false);
    }

    public Optional<PerformanceEvalSet> getFirstEvalSet() {
        return firstEvalSet;
    }

    public void updateFirstPerfEval(List<ItemEval> firstEvals, ItemEval firstTotalEval) {
        this.firstEvalSet = Optional.of(new PerformanceEvalSet(firstEvals, firstTotalEval, false));
    }

    public void rejectSelfEval() {
        selfEvals = Optional.of(selfEvals.get().copy(false));
        firstEvalSet = Optional.empty();
    }

    public void updateFirstPerfEvalDone() {
        firstEvalSet = Optional.of(firstEvalSet.get().copy(true));
    }
    public Optional<PerformanceEvalSet> getSecondEvalSet() {
        return secondEvalSet;
    }

    public void updateSecondPerfEval(List<ItemEval> firstEvals, ItemEval firstTotalEval) {
        this.secondEvalSet = Optional.of(new PerformanceEvalSet(firstEvals, firstTotalEval, false));
    }

    public void updateSecondPerfEvalDone() {
        secondEvalSet = Optional.of(secondEvalSet.get().copy(true));
    }
}
