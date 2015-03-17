package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.domain.evalseason.RateeType;
import net.madvirus.eval.domain.personaleval.AllCompeEvals;
import net.madvirus.eval.domain.personaleval.ItemEval;
import net.madvirus.eval.domain.personaleval.PerformanceItemAndAllEval;
import net.madvirus.eval.domain.personaleval.TotalEval;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;

public abstract class PersonalEvalData implements PersonalEvalState {
    private UserModel ratee;

    public PersonalEvalData(UserModel ratee) {
        this.ratee = ratee;
    }

    @Override
    public UserModel getRatee() {
        return ratee;
    }

    public abstract String getId();

    public abstract String getEvalSeasonId();

    public abstract RateeType getRateeType();

    public abstract List<PerformanceItemAndAllEval> getPerfItemAndAllEvals();

    public abstract AllCompeEvals getAllCompeEvals();

    public abstract ItemEval getFirstPerfTotalEval();

    public abstract ItemEval getSecondPerfTotalEval();

    public abstract TotalEval getFirstTotalEval();
    public abstract TotalEval getSecondTotalEval();

    public abstract Double getFirstMark();
    public abstract Double getSecondMark();

    public abstract boolean checkFirstRater(String firstRaterId);

    public abstract boolean checkSecondRater(String secondRaterId);
}
