package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.CompetencyEvalSet;

public class SelfCompeEvalData {
    private String personalEvalId;
    private String userId;
    private CompetencyEvalSet evalSet;
    private boolean done;

    public SelfCompeEvalData(String personalEvalId, String userId, CompetencyEvalSet evalSet, boolean done) {
        this.personalEvalId = personalEvalId;
        this.userId = userId;
        this.evalSet = evalSet;
        this.done = done;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getUserId() {
        return userId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }

    public boolean isDone() {
        return done;
    }
}
