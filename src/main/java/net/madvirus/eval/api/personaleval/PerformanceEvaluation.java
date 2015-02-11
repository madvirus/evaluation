package net.madvirus.eval.api.personaleval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PerformanceEvaluation {
    private List<PerformanceItem> items;

    private Optional<PerformanceEvalSet> selfEvals;
    private Optional<PerformanceEvalSet> firstEvalSet;

    public PerformanceEvaluation() {
        items = new ArrayList<>();
        selfEvals = Optional.empty();
        firstEvalSet = Optional.empty();
    }

    public List<PerformanceItem> getItems() {
        return items;
    }

    public Optional<PerformanceEvalSet> getSelfEvalSet() {
        return selfEvals;
    }

    public Optional<PerformanceEvalSet> getFirstEvalSet() {
        return firstEvalSet;
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
        return selfEvals.isPresent() ? selfEvals.get().isDone() : false;
    }

    public List<PerformanceItemAndSelfEval> getItemAndSelfEvals() {
        if (items == null || items.isEmpty()) return Collections.emptyList();

        List<PerformanceItemAndSelfEval> result = new ArrayList<>();
        PerformanceEvalSet selfEvalSet = selfEvals.get();
        for (int i = 0; i < items.size(); i++) {
            result.add(new PerformanceItemAndSelfEval(items.get(i), selfEvalSet.getEvals().get(i)));
        }
        return result;
    }

    public List<PerformanceItemAndAllEval> getItemAndAllEvals() {
        List<PerformanceItemAndAllEval> result = new ArrayList<>();
        List<ItemEval> firstItemEvals = firstEvalSet.isPresent() ? firstEvalSet.get().getEvals() : null;
        for (int i = 0; i < items.size(); i++) {
            ItemEval firstEval = firstItemEvals == null ? null : firstItemEvals.get(i);
            result.add(new PerformanceItemAndAllEval(items.get(i), selfEvals.get().getEvals().get(i),
                    firstEval));
        }
        return result;
    }

    public ItemEval getFirstTotalEval() {
        return firstEvalSet.isPresent() ? firstEvalSet.get().getTotalEval() : null;
    }

    public void updateFirstPerfEval(List<ItemEval> firstEvals, ItemEval firstTotalEval) {
        this.firstEvalSet = Optional.of(new PerformanceEvalSet(firstEvals, firstTotalEval, false));
    }

    public void rejectSelfEval() {
        selfEvals = Optional.of(selfEvals.get().copy(false));
        firstEvalSet = Optional.empty();
    }

    public boolean isFirstEvalHad() {
        return firstEvalSet.isPresent();
    }

    public void updateFirstPerfEvalDone() {
        firstEvalSet = Optional.of(firstEvalSet.get().copy(true));
    }
}
