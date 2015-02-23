package net.madvirus.eval.api.personaleval;

public class PersonalEvalSecondRaterChangedEvent {
    private String personalEvalid;
    private String oldSecondRaterId;
    private String newSecondRaterId;

    protected PersonalEvalSecondRaterChangedEvent() {}
    public PersonalEvalSecondRaterChangedEvent(String personalEvalid, String oldSecondRaterId, String newSecondRaterId) {
        this.personalEvalid = personalEvalid;
        this.oldSecondRaterId = oldSecondRaterId;
        this.newSecondRaterId = newSecondRaterId;
    }

    public String getPersonalEvalid() {
        return personalEvalid;
    }

    public String getOldSecondRaterId() {
        return oldSecondRaterId;
    }

    public String getNewSecondRaterId() {
        return newSecondRaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalEvalSecondRaterChangedEvent that = (PersonalEvalSecondRaterChangedEvent) o;

        if (newSecondRaterId != null ? !newSecondRaterId.equals(that.newSecondRaterId) : that.newSecondRaterId != null)
            return false;
        if (oldSecondRaterId != null ? !oldSecondRaterId.equals(that.oldSecondRaterId) : that.oldSecondRaterId != null)
            return false;
        if (personalEvalid != null ? !personalEvalid.equals(that.personalEvalid) : that.personalEvalid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = personalEvalid != null ? personalEvalid.hashCode() : 0;
        result = 31 * result + (oldSecondRaterId != null ? oldSecondRaterId.hashCode() : 0);
        result = 31 * result + (newSecondRaterId != null ? newSecondRaterId.hashCode() : 0);
        return result;
    }
}
