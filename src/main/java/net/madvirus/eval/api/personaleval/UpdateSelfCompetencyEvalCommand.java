package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateSelfCompetencyEvalCommand {
    @JsonProperty
    private String personalEvalId;
    @JsonProperty
    private String evalSeasonId;
    private String userId;

    @JsonProperty
    private CompetencyEvalSet evalSet;
    @JsonProperty
    private boolean done;

    public UpdateSelfCompetencyEvalCommand(String personalEvalId, String evalSeasonId, String userId, CompetencyEvalSet evalSet, boolean done) {
        this.personalEvalId = personalEvalId;
        this.evalSeasonId = evalSeasonId;
        this.userId = userId;
        this.evalSet = evalSet;
        this.done = done;
    }

    protected UpdateSelfCompetencyEvalCommand() {}

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }

    public boolean isDone() {
        return done;
    }
}
