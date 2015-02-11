package net.madvirus.eval.api.personaleval.first;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.api.personaleval.Grade;

public class TotalEvalUpdate {
    @JsonProperty
    private String rateeId;
    @JsonProperty
    private String comment;
    @JsonProperty
    private Grade grade;

    protected TotalEvalUpdate() {}
    public TotalEvalUpdate(String rateeId, String comment, Grade grade) {
        this.rateeId = rateeId;
        this.comment = comment;
        this.grade = grade;
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getComment() {
        return comment;
    }

    public Grade getGrade() {
        return grade;
    }
}
