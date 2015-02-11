package net.madvirus.eval.api.personaleval.first;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.api.personaleval.CompetencyEvalSet;

public class UpdateFirstCompetencyEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String firstRaterId;

    @JsonProperty
    private CompetencyEvalSet evalSet;

    protected UpdateFirstCompetencyEvalCommand() {}
    public UpdateFirstCompetencyEvalCommand(String evalSeasonId, String rateeId, String firstRaterId, CompetencyEvalSet evalSet) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.firstRaterId = firstRaterId;
        this.evalSet = evalSet;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }

    public void setFirstRaterId(String firstRaterId) {
        this.firstRaterId = firstRaterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
