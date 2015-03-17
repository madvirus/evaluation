package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;

public class NotStartedPersonalEvalData extends PersonalEvalData {
    private String evalSeasonId;
    private RateeType rateeType;

    public NotStartedPersonalEvalData(UserModel ratee, String evalSeasonId, RateeType rateeType) {
        super(ratee);
        this.evalSeasonId = evalSeasonId;
        this.rateeType = rateeType;
    }

    @Override
    public UserModel getRatee() {
        return null;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isSelfPerfEvalDone() {
        return false;
    }

    @Override
    public boolean isSelfCompeEvalDone() {
        return false;
    }

    @Override
    public boolean isSelfEvalDone() {
        return false;
    }

    @Override
    public boolean isColleagueCompeEvalDone(String colleagueId) {
        return false;
    }

    @Override
    public boolean isFirstEvalSkipTarget() {
        return false;
    }

    @Override
    public boolean isFirstPerfEvalHad() {
        return false;
    }

    @Override
    public Grade getFirstPerfEvalGrade() {
        return null;
    }

    @Override
    public boolean isFirstCompeEvalHad() {
        return false;
    }

    @Override
    public Grade getFirstCompeEvalGrade() {
        return null;
    }

    @Override
    public Grade getFirstTotalEvalGrade() {
        return null;
    }

    @Override
    public boolean isFirstTotalEvalDone() {
        return false;
    }

    @Override
    public boolean isSecondPerfEvalHad() {
        return false;
    }

    @Override
    public Grade getSecondPerfEvalGrade() {
        return null;
    }

    @Override
    public boolean isSecondCompeEvalHad() {
        return false;
    }

    @Override
    public Grade getSecondCompeEvalGrade() {
        return null;
    }

    @Override
    public Grade getSecondTotalEvalGrade() {
        return null;
    }

    @Override
    public boolean isSecondTotalEvalDone() {
        return false;
    }

    @Override
    public String getId() {
        return PersonalEval.createId(evalSeasonId, getRatee().getId());
    }

    @Override
    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    @Override
    public RateeType getRateeType() {
        return rateeType;
    }

    @Override
    public List<PerformanceItemAndAllEval> getPerfItemAndAllEvals() {
        return null;
    }

    @Override
    public AllCompeEvals getAllCompeEvals() {
        return null;
    }

    @Override
    public ItemEval getFirstPerfTotalEval() {
        return null;
    }

    @Override
    public ItemEval getSecondPerfTotalEval() {
        return null;
    }

    @Override
    public TotalEval getFirstTotalEval() {
        return null;
    }

    @Override
    public TotalEval getSecondTotalEval() {
        return null;
    }

    @Override
    public Double getFirstMark() {
        return null;
    }

    @Override
    public Double getSecondMark() {
        return null;
    }

    @Override
    public boolean checkFirstRater(String firstRaterId) {
        return false;
    }

    @Override
    public boolean checkSecondRater(String secondRaterId) {
        return false;
    }
}
