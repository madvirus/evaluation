package net.madvirus.eval.command.personaleval.self;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class UpdateSelfCompetencyEvalCommand {
    @JsonProperty
    private String evalSeasonId;
    private String rateeId;

    @JsonProperty
    private CompetencyEvalSet evalSet;

    public UpdateSelfCompetencyEvalCommand(String evalSeasonId, String rateeId, CompetencyEvalSet evalSet) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.evalSet = evalSet;
    }

    protected UpdateSelfCompetencyEvalCommand() {
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getRateeId() {
        return rateeId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }

}
