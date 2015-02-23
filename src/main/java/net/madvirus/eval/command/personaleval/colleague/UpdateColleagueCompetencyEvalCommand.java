package net.madvirus.eval.command.personaleval.colleague;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class UpdateColleagueCompetencyEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String raterId;

    @JsonProperty
    private CompetencyEvalSet evalSet;

    protected UpdateColleagueCompetencyEvalCommand() {}
    public UpdateColleagueCompetencyEvalCommand(String evalSeasonId, String rateeId, String raterId, CompetencyEvalSet evalSet) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.raterId = raterId;
        this.evalSet = evalSet;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }

    public void setRaterId(String raterId) {
        this.raterId = raterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getRaterId() {
        return raterId;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
