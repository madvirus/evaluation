package net.madvirus.eval.api.personaleval;

import net.madvirus.eval.api.personaleval.Grade;

public class TotalEval {
    private String comment;
    private Grade grade;
    private boolean done;

    protected TotalEval() {}
    public TotalEval(String comment, Grade grade, boolean done) {
        this.comment = comment;
        this.grade = grade;
        this.done = done;
    }

    public String getComment() {
        return comment;
    }

    public Grade getGrade() {
        return grade;
    }

    public boolean isDone() {
        return done;
    }
}
