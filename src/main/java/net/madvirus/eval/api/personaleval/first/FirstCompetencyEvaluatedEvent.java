package net.madvirus.eval.api.personaleval.first;

import net.madvirus.eval.api.personaleval.CompetencyEvalSet;

public class FirstCompetencyEvaluatedEvent {
    private String personalEvalId;
    private String firstRaterId;
    private CompetencyEvalSet evalSet;

    protected FirstCompetencyEvaluatedEvent() {}

    public FirstCompetencyEvaluatedEvent(String personalEvalId, String firstRaterId, CompetencyEvalSet evalSet) {
        this.personalEvalId = personalEvalId;
        this.firstRaterId = firstRaterId;
        this.evalSet = evalSet;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
