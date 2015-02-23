package net.madvirus.eval.api.personaleval;

public class PersonalEvalFirstRaterChangedEvent {
    private String personalEvalid;
    private String oldFirstRaterId;
    private String newFirstRaterId;

    protected PersonalEvalFirstRaterChangedEvent() {}
    public PersonalEvalFirstRaterChangedEvent(String personalEvalid, String oldFirstRaterId, String newFirstRaterId) {
        this.personalEvalid = personalEvalid;
        this.oldFirstRaterId = oldFirstRaterId;
        this.newFirstRaterId = newFirstRaterId;
    }

    public String getPersonalEvalid() {
        return personalEvalid;
    }

    public String getOldFirstRaterId() {
        return oldFirstRaterId;
    }

    public String getNewFirstRaterId() {
        return newFirstRaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalEvalFirstRaterChangedEvent that = (PersonalEvalFirstRaterChangedEvent) o;

        if (newFirstRaterId != null ? !newFirstRaterId.equals(that.newFirstRaterId) : that.newFirstRaterId != null)
            return false;
        if (oldFirstRaterId != null ? !oldFirstRaterId.equals(that.oldFirstRaterId) : that.oldFirstRaterId != null)
            return false;
        if (personalEvalid != null ? !personalEvalid.equals(that.personalEvalid) : that.personalEvalid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = personalEvalid != null ? personalEvalid.hashCode() : 0;
        result = 31 * result + (oldFirstRaterId != null ? oldFirstRaterId.hashCode() : 0);
        result = 31 * result + (newFirstRaterId != null ? newFirstRaterId.hashCode() : 0);
        return result;
    }
}
