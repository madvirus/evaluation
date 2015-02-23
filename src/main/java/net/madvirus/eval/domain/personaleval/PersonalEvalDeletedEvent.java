package net.madvirus.eval.domain.personaleval;

public class PersonalEvalDeletedEvent {
    private String personalEvalId;

    public PersonalEvalDeletedEvent(String personalEvalId) {
        this.personalEvalId = personalEvalId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }
}
