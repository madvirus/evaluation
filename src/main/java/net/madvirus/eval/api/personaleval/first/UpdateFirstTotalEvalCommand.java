package net.madvirus.eval.api.personaleval.first;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateFirstTotalEvalCommand {
    private String evalSeasonId;
    private String firstRaterId;
    @JsonProperty
    private List<TotalEvalUpdate> evalUpdates;
    @JsonProperty
    private boolean done;

    protected UpdateFirstTotalEvalCommand() {}
    public UpdateFirstTotalEvalCommand(String evalSeasonId, String firstRaterId, List<TotalEvalUpdate> evalUpdates, boolean done) {
        this.evalSeasonId = evalSeasonId;
        this.firstRaterId = firstRaterId;
        this.evalUpdates = evalUpdates;
        this.done = done;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setFirstRaterId(String firstRaterId) {
        this.firstRaterId = firstRaterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public List<TotalEvalUpdate> getEvalUpdates() {
        return evalUpdates;
    }

    public boolean isDone() {
        return done;
    }
}
