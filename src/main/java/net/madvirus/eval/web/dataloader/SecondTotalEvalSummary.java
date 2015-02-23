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
        return secondPerfEval.flatMap(eval -> Optional.of(true)).orElse(false);
    }

    public Grade getSecondPerfEvalGrade() {
        return secondPerfEval.flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public boolean isSecondCompeEvalHad() {
        return secondCompeEval.flatMap(eval -> Optional.of(true)).orElse(false);
    }

    public Double getSecondCompeCommonAvg() {
        return secondCompeEval
                .flatMap(eval -> Optional.of(calculateAverage(eval.getCommonsEvals())))
                .orElse(null);
    }

    public Double getSecondCompeLeadershipAvg() {
        if (!rateeType.hasLeadership()) return null;
        return secondCompeEval
                .flatMap(eval -> Optional.of(calculateAverage(eval.getLeadershipEvals())))
                .orElse(null);
    }

    public Double getSecondCompeAmAvg() {
        if (!rateeType.hasAm()) return null;
        return secondCompeEval
                .flatMap(eval -> Optional.of(calculateAverage(eval.getAmEvals())))
                .orElse(null);
    }

    private Double calculateAverage(List<ItemEval> evals) {
        int count = 0;
        double sum = 0.0;

        for (ItemEval itemEval : evals) {
            sum += itemEval.getGrade().getNumber();
            count ++;
        }
        return sum / count;
    }

    public Grade getSecondCompeEvalGrade() {
        return secondCompeEval.flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Double getSecondTotalMark() {
        return MarkCalculator.calculate(rateeType,
                getSecondPerfEvalGrade(),
                getSecondCompeCommonAvg(),
                getSecondCompeLeadershipAvg(),
                getSecondCompeAmAvg());
    }

    public TotalEval getSecondTotalEval() {
        return secondTotalEval.orElse(null);
    }

    public boolean isSecondTotalEvalDone() {
        return secondTotalEval.flatMap(eval -> Optional.of(eval.isDone())).orElse(false);
    }

}
