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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEval itemEval = (ItemEval) o;

        if (comment != null ? !comment.equals(itemEval.comment) : itemEval.comment != null) return false;
        if (grade != itemEval.grade) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = comment != null ? comment.hashCode() : 0;
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        return result;
    }

    public static ItemEval empty() {
        return new ItemEval("", Grade.A);
    }
}
