package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompetencyEvalSet {
    @JsonProperty
    private List<ItemEval> commonsEvals;
    @JsonProperty
    private List<ItemEval> leadershipEvals;
    @JsonProperty
    private List<ItemEval> amEvals;
    @JsonProperty
    private ItemEval totalEval;
    @JsonProperty
    private boolean done;

    protected CompetencyEvalSet() {
    }

    public CompetencyEvalSet(
            List<ItemEval> commonsEvals,
            List<ItemEval> leadershipEvals,
            List<ItemEval> amEvals,
            ItemEval totalEval,
            boolean done) {
        this.commonsEvals = copyList(commonsEvals);
        this.leadershipEvals = copyList(leadershipEvals);
        this.amEvals = copyList(amEvals);
        this.totalEval = totalEval;
        this.done = done;
    }

    private List<ItemEval> copyList(List<ItemEval> evals) {
        if (evals == null) return null;
        else return new ArrayList<>(evals);
    }

    public List<ItemEval> getCommonsEvals() {
        return unmodifiableList(commonsEvals);
    }

    private List<ItemEval> unmodifiableList(List<ItemEval> evals) {
        return evals == null ? null : Collections.unmodifiableList(evals);
    }

    public List<ItemEval> getLeadershipEvals() {
        return unmodifiableList(leadershipEvals);
    }

    public List<ItemEval> getAmEvals() {
        return unmodifiableList(amEvals);
    }

    public ItemEval getTotalEval() {
        return totalEval;
    }

    public boolean isDone() {
        return done;
    }

    public CompetencyEvalSet copy(boolean done) {
        return new CompetencyEvalSet(commonsEvals, leadershipEvals, amEvals, totalEval, done);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompetencyEvalSet that = (CompetencyEvalSet) o;

        if (done != that.done) return false;
        if (amEvals != null ? !amEvals.equals(that.amEvals) : that.amEvals != null) return false;
        if (commonsEvals != null ? !commonsEvals.equals(that.commonsEvals) : that.commonsEvals != null) return false;
        if (leadershipEvals != null ? !leadershipEvals.equals(that.leadershipEvals) : that.leadershipEvals != null)
            return false;
        if (totalEval != null ? !totalEval.equals(that.totalEval) : that.totalEval != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commonsEvals != null ? commonsEvals.hashCode() : 0;
        result = 31 * result + (leadershipEvals != null ? leadershipEvals.hashCode() : 0);
        result = 31 * result + (amEvals != null ? amEvals.hashCode() : 0);
        result = 31 * result + (totalEval != null ? totalEval.hashCode() : 0);
        result = 31 * result + (done ? 1 : 0);
        return result;
    }
}
