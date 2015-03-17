package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.domain.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;
import java.util.Optional;

public class SecondTotalEvalSummary extends FirstTotalEvalSummary {

    private Optional<PerformanceEvalSet> secondPerfEval;
    private Optional<CompetencyEvalSet> secondCompeEval;
    private Optional<TotalEval> secondTotalEval;

    public SecondTotalEvalSummary(UserModel ratee, PersonalEval personalEval) {
        super(ratee, personalEval);
        secondPerfEval = personalEval.getSecondPerfEvalSet();
        secondCompeEval = personalEval.getSecondCompeEvalSet();
        secondTotalEval = personalEval.getSecondTotalEval();
    }

    public boolean isSecondPerfEvalHad() {
        return personalEval.isSecondPerfEvalHad();
    }

    public Grade getSecondPerfEvalGrade() {
        return personalEval.getSecondPerfEvalGrade();
    }

    public boolean isSecondCompeEvalHad() {
        return personalEval.isSecondCompeEvalHad();
    }

    public Double getSecondCompeCommonAvg() {
        return personalEval.getSecondCompeEvalSet().flatMap(eval -> Optional.ofNullable(eval.getCommonsAverage())).orElse(null);
    }

    public Double getSecondCompeLeadershipAvg() {
        if (!rateeType.hasLeadership()) return null;
        return personalEval.getSecondCompeEvalSet().flatMap(eval -> Optional.ofNullable(eval.getLeadershipAverage())).orElse(null);
    }

    public Double getSecondCompeAmAvg() {
        if (!rateeType.hasAm()) return null;
        return personalEval.getSecondCompeEvalSet().flatMap(eval -> Optional.ofNullable(eval.getAmAverage())).orElse(null);
    }

    public Grade getSecondCompeEvalGrade() {
        return personalEval.getSecondCompeEvalGrade();
    }

    public Double getSecondTotalMark() {
        return personalEval.getSecondMark();
    }

    public TotalEval getSecondTotalEval() {
        return personalEval.getSecondTotalEval().orElse(null);
    }

    public boolean isSecondTotalEvalDone() {
        return personalEval.isSecondTotalEvalDone();
    }

}
