package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.query.user.UserModel;

public class ColleagueEvalState {
    private UserModel ratee;
    private boolean personalEvalStarted;
    private boolean selfCompeEvalDone;
    private boolean colleagueEvalDone;

    public ColleagueEvalState(UserModel ratee, boolean personalEvalStarted, boolean selfCompeEvalDone, boolean colleagueEvalDone) {
        this.ratee = ratee;
        this.personalEvalStarted = personalEvalStarted;
        this.selfCompeEvalDone = selfCompeEvalDone;
        this.colleagueEvalDone = colleagueEvalDone;
    }

    public UserModel getRatee() {
        return ratee;
    }

    public boolean isPersonalEvalStarted() {
        return personalEvalStarted;
    }

    public boolean isSelfCompeEvalDone() {
        return selfCompeEvalDone;
    }

    public boolean isColleagueEvalDone() {
        return colleagueEvalDone;
    }
}
