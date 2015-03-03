package net.madvirus.eval.api.personaleval;

public class FirstDraftReturnedEvent {
    private String personalEvalId;

    protected FirstDraftReturnedEvent() {
    }
    public FirstDraftReturnedEvent(String personalEvalId) {
        this.personalEvalId = personalEvalId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }
}
