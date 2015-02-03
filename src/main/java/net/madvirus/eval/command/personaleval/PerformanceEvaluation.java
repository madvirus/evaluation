package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.personaleval.ItemEval;
import net.madvirus.eval.api.personaleval.PerformanceItem;
import net.madvirus.eval.api.personaleval.PerformanceItemAndSelfEval;

import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluation {
    private List<PerformanceItem> items;
    private List<ItemEval> selfEvals;
    private boolean selfEvalDone;

    public PerformanceEvaluation() {
        items = new ArrayList<>();
        selfEvals = new ArrayList<>();
    }

    public void updateSelfPerfEval(List<PerformanceItemAndSelfEval> perfItemAndSelfEvals, boolean done) {
        items.clear();
        selfEvals.clear();
        perfItemAndSelfEvals.forEach(ie -> {
            items.add(ie.getItem());
            selfEvals.add(ie.getEval());
        });
        this.selfEvalDone = done;
    }

    public boolean isSelfEvalDone() {
        return selfEvalDone;
    }

    public List<PerformanceItemAndSelfEval> getItemAndSelfEvals() {
        List<PerformanceItemAndSelfEval> result = new ArrayList<>();
        for (int i = 0 ; i < items.size() ; i++) {
            result.add(new PerformanceItemAndSelfEval(items.get(i), selfEvals.get(i)));
        }
        return result;
    }

}
