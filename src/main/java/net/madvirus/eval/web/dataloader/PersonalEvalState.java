package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;
import net.madvirus.eval.query.user.UserModel;

public interface PersonalEvalState {
    UserModel getRatee();
    boolean isStarted();
    boolean isSelfPerfEvalDone();
    boolean isSelfCompeEvalDone();
    boolean isSelfEvalDone();

    boolean isColleagueCompeEvalDone(String colleagueId);

    boolean isFirstEvalSkipTarget();

    boolean isFirstPerfEvalHad();
    Grade getFirstPerfEvalGrade();
    boolean isFirstCompeEvalHad();
    Grade getFirstCompeEvalGrade();
    Grade getFirstTotalEvalGrade();
    boolean isFirstTotalEvalDone();

    boolean isSecondPerfEvalHad();
    Grade getSecondPerfEvalGrade();
    boolean isSecondCompeEvalHad();
    Grade getSecondCompeEvalGrade();
    Grade getSecondTotalEvalGrade();
    boolean isSecondTotalEvalDone();
}

class PersonalEvalStateBuilder {
    static PersonalEvalState notStarted(UserModel ratee) {
        return new PersonalEvalState() {
            @Override
            public UserModel getRatee() {
                return ratee;
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
        };
    }
}
