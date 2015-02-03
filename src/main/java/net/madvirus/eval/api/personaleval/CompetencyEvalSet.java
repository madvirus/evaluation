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

    protected CompetencyEvalSet() {
    }

    public CompetencyEvalSet(List<ItemEval> commonsEvals, List<ItemEval> leadershipEvals, List<ItemEval> amEvals) {
        this.commonsEvals = copyList(commonsEvals);
        this.leadershipEvals = copyList(leadershipEvals);
        this.amEvals = copyList(amEvals);
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
}
