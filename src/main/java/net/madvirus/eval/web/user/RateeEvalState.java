package net.madvirus.eval.web.user;

import net.madvirus.eval.query.user.UserModel;
import net.madvirus.eval.web.dataloader.PersonalEvalState;

public class RateeEvalState {
    private final UserModel user;
    private final PersonalEvalState state;

    public RateeEvalState(UserModel user, PersonalEvalState state) {
        this.user = user;
        this.state = state;
    }

    public UserModel getUser() {
        return user;
    }

    public PersonalEvalState getState() {
        return state;
    }
}
