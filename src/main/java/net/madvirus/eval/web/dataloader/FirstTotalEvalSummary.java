package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;
import java.util.Optional;

public class FirstTotalEvalSummary {

    private UserModel ratee;
    private RateeType rateeType;
    private Optional<PerformanceEvalSet> firstPerfEval;
    private Optional<CompetencyEvalSet> firstCompeEval;
    private Optional<TotalEval> firstTotalEval;

    public FirstTotalEvalSummary(UserModel ratee, PersonalEval personalEval) {
        this.ratee = ratee;
        this.rateeType = personalEval.getRateeType();
        firstPerfEval = personalEval.getFirstPerfEvalSet();
        firstCompeEval = personalEval.getFirstCompeEvalSet();
        firstTotalEval = personalEval.getFirstTotalEval();
    }

    public UserModel getRatee() {
        return ratee;
    }

    public boolean isFirstPerfEvalHad() {
        return firstPerfEval.flatMap(eval -> Optional.of(true)).orElse(false);
    }

    public Grade getFirstPerfEvalGrade() {
        return firstPerfEval.flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public boolean isFirstCompeEvalHad() {
        return firstCompeEval.flatMap(eval -> Optional.of(true)).orElse(false);
    }

    public Double getFirstCompeCommonAvg() {
        if (!firstCompeEval.isPresent()) return null;
        return calculateAverage(firstCompeEval.get().getCommonsEvals());
    }

    public Double getFirstCompeLeadershipAvg() {
        if (!firstCompeEval.isPresent()) return null;
        if (!rateeType.hasLeadership()) return null;
        return calculateAverage(firstCompeEval.get().getLeadershipEvals());
    }

    public Double getFirstCompeAmAvg() {
        if (!firstCompeEval.isPresent()) return null;
        if (!rateeType.hasAm()) return null;
        return calculateAverage(firstCompeEval.get().getAmEvals());
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

    public Grade getFirstCompeEvalGrade() {
        return firstCompeEval.flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    public Double calculateTotalMark() {
        return MarkCalculator.calculate(rateeType,
                getFirstPerfEvalGrade(),
                getFirstCompeCommonAvg(),
                getFirstCompeLeadershipAvg(),
                getFirstCompeAmAvg());
    }

    public TotalEval getFirstTotalEval() {
        return firstTotalEval.orElse(null);
    }

    public boolean isFirstTotalEvalDone() {
        return firstTotalEval.flatMap(eval -> Optional.of(eval.isDone())).orElse(false);
    }
}
