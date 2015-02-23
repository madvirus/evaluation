package net.madvirus.eval.api.personaleval.colleague;

import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class ColleagueCompetencyEvaluatedEvent {
    private String personalEvalId;
    private String colleagueRaterId;
    private CompetencyEvalSet evalSet;

    protected ColleagueCompetencyEvaluatedEvent() {}

    public ColleagueCompetencyEvaluatedEvent(String personalEvalId, String colleagueRaterId, CompetencyEvalSet evalSet) {
        this.personalEvalId = personalEvalId;
        this.colleagueRaterId = colleagueRaterId;
        this.evalSet = evalSet;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getColleagueRaterId() {
        return colleagueRaterId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
