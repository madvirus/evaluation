package net.madvirus.eval.api.personaleval;

import java.util.List;

public class SelfCompetencyEvaluatedEvent {
    private String personalEvalId;
    private boolean done;
    private CompetencyEvalSet evalSet;

    protected SelfCompetencyEvaluatedEvent() {}

    public SelfCompetencyEvaluatedEvent(String personalEvalId, boolean done, CompetencyEvalSet evalSet) {
        this.personalEvalId = personalEvalId;
        this.done = done;
        this.evalSet = evalSet;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public boolean isDone() {
        return done;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
