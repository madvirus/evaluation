package net.madvirus.eval.web.dataloader;

import java.util.List;

public class SecondTotalEvalData {

    private List<SecondTotalEvalSummary> evalSummaryList;

    public SecondTotalEvalData(List<SecondTotalEvalSummary> evalSummaryList) {
        this.evalSummaryList = evalSummaryList;
    }

    public List<SecondTotalEvalSummary> getEvalSummaries() {
        return evalSummaryList;
    }

    public boolean isAllPerfEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isSecondPerfEvalHad());
    }

    public boolean isAllCompeEvalHad() {
        return evalSummaryList.stream().allMatch(x -> x.isSecondCompeEvalHad());
    }

    public boolean isTotalEvalAvailable() {
        return isAllPerfEvalHad() && isAllCompeEvalHad();
    }

    public boolean isTotalEvalDone() {
        return evalSummaryList.stream().allMatch(x -> x.isSecondTotalEvalDone());
    }
}
