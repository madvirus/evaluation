package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.CompetencyEvalSet;

public class CompeEvalData {
    private String evalSeasonId;
    private String personalEvalId;
    private String userId;
    private RateeType rateeType;
    private CompetencyEvalSet evalSet;

    public CompeEvalData(String evalSeasonId, String personalEvalId, String userId, RateeType rateeType, CompetencyEvalSet evalSet) {
        this.evalSeasonId = evalSeasonId;
        this.personalEvalId = personalEvalId;
        this.userId = userId;
        this.rateeType = rateeType;
        this.evalSet = evalSet;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getUserId() {
        return userId;
    }

    public RateeType getRateeType() {
        return rateeType;
    }

    public CompetencyEvalSet getEvalSet() {
        return evalSet;
    }
}
