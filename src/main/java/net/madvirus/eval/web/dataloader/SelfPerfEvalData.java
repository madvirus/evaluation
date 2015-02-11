package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.PerformanceItemAndSelfEval;

import java.util.List;

public class SelfPerfEvalData {
    private String evalSeasonId;
    private String personalEvalId;
    private String userId;
    private List<PerformanceItemAndSelfEval> itemAndEvals;
    private boolean done;

    public SelfPerfEvalData(
            String evalSeasonId,
            String personalEvalId,
            String userId,
            List<PerformanceItemAndSelfEval> itemAndEvals,
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
        return itemAndEvals;
    }

    public boolean isDone() {
        return done;
    }
}
