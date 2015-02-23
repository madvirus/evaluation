package net.madvirus.eval.api.personaleval.second;

import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class SecondCompetencyEvaluatedEvent {
    private String personalEvalId;
    private String firstRaterId;
    private CompetencyEvalSet evalSet;

    protected SecondCompetencyEvaluatedEvent() {}

    public SecondCompetencyEvaluatedEvent(String personalEvalId, String firstRaterId, CompetencyEvalSet evalSet) {
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
