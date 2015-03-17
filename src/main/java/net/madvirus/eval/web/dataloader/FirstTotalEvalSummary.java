package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;
import java.util.Optional;

public class FirstTotalEvalSummary {

    private UserModel ratee;
    protected RateeType rateeType;
    protected PersonalEval personalEval;

    public FirstTotalEvalSummary(UserModel ratee, PersonalEval personalEval) {
        this.ratee = ratee;
        this.personalEval = personalEval;
        this.rateeType = personalEval.getRateeType();
    }

    public UserModel getRatee() {
        return ratee;
    }

    public boolean isFirstPerfEvalHad() {
        return personalEval.isFirstPerfEvalHad();
    }

    public Grade getFirstPerfEvalGrade() {
        return personalEval.getFirstPerfEvalGrade();
    }

    public boolean isFirstCompeEvalHad() {
        return personalEval.isFirstCompeEvalHad();
    }

    public Double getFirstCompeCommonAvg() {
        return personalEval.getFirstCompeEvalSet()
                .flatMap(evalSet -> Optional.ofNullable(evalSet.getCommonsAverage()))
                .orElse(null);
    }

    public Double getFirstCompeLeadershipAvg() {
        if (!rateeType.hasLeadership()) return null;
        return personalEval.getFirstCompeEvalSet()
                .flatMap(evalSet -> Optional.ofNullable(evalSet.getLeadershipAverage()))
                .orElse(null);
    }

    public Double getFirstCompeAmAvg() {
        if (!rateeType.hasAm()) return null;
        return personalEval.getFirstCompeEvalSet()
                .flatMap(evalSet -> Optional.ofNullable(evalSet.getAmAverage()))
                .orElse(null);
    }

    public Grade getFirstCompeEvalGrade() {
        return personalEval.getFirstCompeEvalGrade();
    }

    public Double getFirstTotalMark() {
        return personalEval.getFirstMark();
    }

    public TotalEval getFirstTotalEval() {
        return personalEval.getFirstTotalEval().orElse(null);
    }

    public boolean isFirstTotalEvalDone() {
        return personalEval.isFirstTotalEvalDone();
    }
}
