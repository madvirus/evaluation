package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.personaleval.PerformanceItemAndAllEval;
import net.madvirus.eval.domain.personaleval.PerformanceItemAndSelfEval;

import java.util.List;
import java.util.stream.Collectors;

public class SelfPerfEvalData {
    private String evalSeasonId;
    private String personalEvalId;
    private String userId;
    private List<PerformanceItemAndAllEval> itemAndEvals;
    private boolean done;

    public SelfPerfEvalData(
            String evalSeasonId,
            String personalEvalId,
            String userId,
            List<PerformanceItemAndAllEval> itemAndEvals,
            boolean done) {
        this.evalSeasonId = evalSeasonId;
        this.personalEvalId = personalEvalId;
        this.userId = userId;
        this.itemAndEvals = itemAndEvals;
        this.done = done;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getUserId() {
        return userId;
    }

    public List<PerformanceItemAndSelfEval> getItemAndEvals() {
        return itemAndEvals
                .stream()
                .map(ie -> new PerformanceItemAndSelfEval(ie.getItem(), ie.getSelfEval()))
                .collect(Collectors.toList());
    }

    public boolean isDone() {
        return done;
    }
}
