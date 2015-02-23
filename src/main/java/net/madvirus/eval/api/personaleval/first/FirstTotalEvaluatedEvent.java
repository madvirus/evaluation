package net.madvirus.eval.api.personaleval.first;

import net.madvirus.eval.domain.personaleval.TotalEval;

public class FirstTotalEvaluatedEvent {
    private String personalEvalId;
    private TotalEval totalEval;

    protected FirstTotalEvaluatedEvent() {}
    public FirstTotalEvaluatedEvent(String personalEvalId, TotalEval totalEval) {
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
