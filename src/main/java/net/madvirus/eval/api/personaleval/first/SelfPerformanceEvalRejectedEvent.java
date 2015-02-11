package net.madvirus.eval.api.personaleval.first;

public class SelfPerformanceEvalRejectedEvent {
    private String personalEvalId;
    private String rateeId;
    private String raterId;

    protected SelfPerformanceEvalRejectedEvent() {}
    public SelfPerformanceEvalRejectedEvent(String personalEvalId, String rateeId, String raterId) {
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
