package net.madvirus.eval.api.personaleval;

import net.madvirus.eval.api.evalseaon.RateeType;

public class PersonalEvaluationCreatedEvent {
    private String personalEvalId;
    private String evalSeasonId;
    private String userId;
    private RateeType rateeType;
    private String firstRaterId;
    private String secondRaterId;

    protected PersonalEvaluationCreatedEvent() {}

    public PersonalEvaluationCreatedEvent(String personalEvalId, String evalSeasonId, String userId, RateeType rateeType, String firstRaterId, String secondRaterId) {
        this.personalEvalId = personalEvalId;
        this.evalSeasonId = evalSeasonId;
        this.userId = userId;
        this.rateeType = rateeType;
        this.firstRaterId = firstRaterId;
        this.secondRaterId = secondRaterId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getUserId() {
        return userId;
    }

    public RateeType getRateeType() {
        return rateeType;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public String getSecondRaterId() {
        return secondRaterId;
    }
}
