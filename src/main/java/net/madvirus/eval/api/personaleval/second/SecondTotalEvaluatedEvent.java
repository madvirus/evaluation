package net.madvirus.eval.api.personaleval.second;

import net.madvirus.eval.domain.personaleval.TotalEval;

public class SecondTotalEvaluatedEvent {
    private String personalEvalId;
    private TotalEval totalEval;

    protected SecondTotalEvaluatedEvent() {}
    public SecondTotalEvaluatedEvent(String personalEvalId, TotalEval totalEval) {
        this.personalEvalId = personalEvalId;
        this.totalEval = totalEval;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public TotalEval getTotalEval() {
        return totalEval;
    }
}
