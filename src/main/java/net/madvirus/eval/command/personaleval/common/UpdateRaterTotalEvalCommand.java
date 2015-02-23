package net.madvirus.eval.command.personaleval.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class UpdateRaterTotalEvalCommand {
    private String evalSeasonId;
    private String raterId;
    @JsonProperty
    private List<TotalEvalUpdate> evalUpdates;
    @JsonProperty
    private boolean done;

    protected UpdateRaterTotalEvalCommand() {}
    public UpdateRaterTotalEvalCommand(String evalSeasonId, String raterId, List<TotalEvalUpdate> evalUpdates, boolean done) {
        this.evalSeasonId = evalSeasonId;
        this.raterId = raterId;
        this.evalUpdates = evalUpdates;
        this.done = done;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setRaterId(String raterId) {
        this.raterId = raterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getRaterId() {
        return raterId;
    }

    public List<TotalEvalUpdate> getEvalUpdates() {
        return evalUpdates;
    }

    public boolean isDone() {
        return done;
    }
}
