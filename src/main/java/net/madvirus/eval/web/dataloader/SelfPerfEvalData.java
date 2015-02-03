package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.PerformanceItemAndSelfEval;

import java.util.List;

public class SelfPerfEvalData {
    private  String personalEvalId;
    private String userId;
    private List<PerformanceItemAndSelfEval> itemAndEvals;
    private boolean done;

    public SelfPerfEvalData(
            String personalEvalId,
            String userId,
            List<PerformanceItemAndSelfEval> itemAndEvals,
            boolean done) {
        this.personalEvalId = personalEvalId;
        this.userId = userId;
        this.itemAndEvals = itemAndEvals;
        this.done = done;
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
