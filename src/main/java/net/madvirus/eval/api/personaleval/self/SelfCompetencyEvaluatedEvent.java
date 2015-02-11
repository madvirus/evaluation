package net.madvirus.eval.api.personaleval.self;

import net.madvirus.eval.api.personaleval.CompetencyEvalSet;

public class SelfCompetencyEvaluatedEvent {
    private String personalEvalId;
    private CompetencyEvalSet evalSet;

    protected SelfCompetencyEvaluatedEvent() {}

    public SelfCompetencyEvaluatedEvent(String personalEvalId, CompetencyEvalSet evalSet) {
        this.personalEvalId = personalEvalId;
        this.evalSet = evalSet;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
