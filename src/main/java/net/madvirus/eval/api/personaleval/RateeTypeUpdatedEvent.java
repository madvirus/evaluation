package net.madvirus.eval.api.personaleval;

import net.madvirus.eval.domain.evalseason.RateeType;

public class RateeTypeUpdatedEvent {
    private String personalEvalId;
    private RateeType rateeType;

    protected RateeTypeUpdatedEvent() {}
    public RateeTypeUpdatedEvent(String personalEvalId, RateeType rateeType) {
        this.personalEvalId = personalEvalId;
        this.rateeType = rateeType;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public RateeType getRateeType() {
        return rateeType;
    }
}
