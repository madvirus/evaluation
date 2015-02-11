package net.madvirus.eval.api.personaleval.first;

public class SelfCompetencyEvalRejectedEvent {
    private String personalEvalId;
    private String rateeId;
    private String raterId;

    protected SelfCompetencyEvalRejectedEvent() {}
    public SelfCompetencyEvalRejectedEvent(String personalEvalId, String rateeId, String raterId) {
        this.personalEvalId = personalEvalId;
        this.rateeId = rateeId;
        this.raterId = raterId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getRaterId() {
        return raterId;
    }
}
