package net.madvirus.eval.web.dataloader;

import java.util.List;

public class FirstTotalEvalData {
    private List<FirstTotalEvalSummary> evalSummaryList;

    public FirstTotalEvalData(List<FirstTotalEvalSummary> evalSummaryList) {
        this.evalSummaryList = evalSummaryList;
    }

    public List<FirstTotalEvalSummary> getEvalSummaries() {
        return evalSummaryList;
    }

    public boolean isAllPerfEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstPerfEvalHad());
    }

    public boolean isAllCompeEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstCompeEvalHad());
    }

    public boolean isTotalEvalAvailable() {
        return isAllPerfEvalHad() && isAllCompeEvalHad();
    }

    public boolean isTotalEvalDone() {
        return evalSummaryList.stream().allMatch(x -> x.isFirstTotalEvalDone());
    }
}
