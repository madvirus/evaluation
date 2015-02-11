package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.evalseaon.RateeType;
import net.madvirus.eval.api.personaleval.*;
import net.madvirus.eval.query.user.UserModel;

import java.util.List;
import java.util.Optional;

public class PersonalEvalData implements PersonalEvalState {
    private PersonalEval personalEval;
    private UserModel ratee;

    public PersonalEvalData(PersonalEval personalEval, UserModel ratee) {
        this.personalEval = personalEval;
        this.ratee = ratee;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isSelfPerfEvalDone() {
        return personalEval.isSelfPerfEvalDone();
    }

    @Override
    public boolean isSelfCompeEvalDone() {
        return personalEval.isSelfCompeEvalDone();
    }

    @Override
    public boolean isColleagueCompeEvalDone(String colleagueId) {
        return personalEval.isColleagueCompeEvalDone(colleagueId);
    }

    @Override
    public boolean isFirstPerfEvalHad() {
        return personalEval.getFirstPerfEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getFirstPerfEvalGrade() {
        return personalEval.getFirstPerfEvalSet()
                .flatMap(eval -> Optional.of(eval.getTotalEval().getGrade())).orElse(null);
    }

    @Override
    public boolean isFirstCompeEvalHad() {
        return personalEval.getFirstCompeEvalSet()
                .flatMap(eval -> Optional.of(true)).orElse(false);
    }

    @Override
    public Grade getFirstCompeEvalGrade() {
        return personalEval.getFirstCompeEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval().getGrade())).orElse(null);
    }

    @Override
    public Grade getFirstTotalEvalGrade() {
        return personalEval.getFirstTotalEval()
                .flatMap(eval -> Optional.ofNullable(eval.getGrade())).orElse(null);
    }

    @Override
    public boolean isFirstTotalEvalDone() {
        return personalEval.getFirstTotalEval().flatMap(evl -> Optional.of(evl.isDone())).orElse(false);
    }

    public UserModel getRatee() {
        return ratee;
    }

    public String getId() {
        return personalEval.getId();
    }

    public String getEvalSeasonId() {
        return personalEval.getEvalSeasonId();
    }

    public RateeType getRateeType() {
        return personalEval.getRateeType();
    }

    public List<PerformanceItemAndSelfEval> getPerfItemAndSelfEvals() {
        return personalEval.getPerfItemAndSelfEvals();
    }

    public List<PerformanceItemAndAllEval> getPerfItemAndAllEvals() {
        return personalEval.getPerfItemAndAllEvals();
    }

    public AllCompeEvals getAllCompeEvals() {
        return personalEval.getAllCompeEvals();
    }

    public ItemEval getFirstPerfTotalEval() {
        return personalEval.getFirstPerfEvalSet()
                .flatMap(eval -> Optional.ofNullable(eval.getTotalEval())).orElse(null);
    }

    // TODO FirstRater인지 확인하는 기능 EvalSeason으로 이동 필요
    public boolean checkFirstRater(String firstRaterId) {
        return personalEval.checkFirstRater(firstRaterId);
    }

}
