package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemEval {
    @JsonProperty
    private String comment;
    @JsonProperty
    private Grade grade;

    protected ItemEval() {}
    public ItemEval(String comment, Grade grade) {
        this.comment = comment;
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public Grade getGrade() {
        return grade;
    }
}
