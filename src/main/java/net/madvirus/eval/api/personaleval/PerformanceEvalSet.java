package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PerformanceEvalSet {
    @JsonProperty
    private List<ItemEval> evals;
    @JsonProperty
    private ItemEval totalEval;
    @JsonProperty
    private boolean done;

    protected PerformanceEvalSet() {}

    public PerformanceEvalSet(List<ItemEval> evals, ItemEval totalEval, boolean done) {
        this.evals = evals;
        this.totalEval = totalEval;
        this.done = done;
    }

    public List<ItemEval> getEvals() {
        return evals;
    }

    public ItemEval getTotalEval() {
        return totalEval;
    }

    public boolean isDone() {
        return done;
    }

    public PerformanceEvalSet copy(boolean done) {
        return new PerformanceEvalSet(this.evals, this.totalEval, done);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerformanceEvalSet that = (PerformanceEvalSet) o;

        if (done != that.done) return false;
        if (evals != null ? !evals.equals(that.evals) : that.evals != null) return false;
        if (totalEval != null ? !totalEval.equals(that.totalEval) : that.totalEval != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evals != null ? evals.hashCode() : 0;
        result = 31 * result + (totalEval != null ? totalEval.hashCode() : 0);
        result = 31 * result + (done ? 1 : 0);
        return result;
    }
}
