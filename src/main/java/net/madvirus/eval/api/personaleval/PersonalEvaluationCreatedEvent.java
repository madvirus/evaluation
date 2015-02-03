package net.madvirus.eval.api.personaleval;

public class PersonalEvaluationCreatedEvent {
    private String personalEvalId;
    private String evalSeasonId;
    private String userId;

    protected PersonalEvaluationCreatedEvent() {}

    public PersonalEvaluationCreatedEvent(String personalEvalId, String evalSeasonId, String userId) {
        this.personalEvalId = personalEvalId;
        this.evalSeasonId = evalSeasonId;
        this.userId = userId;
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
}
