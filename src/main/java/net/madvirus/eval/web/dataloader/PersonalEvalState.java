package net.madvirus.eval.web.dataloader;

import net.madvirus.eval.api.personaleval.Grade;

public interface PersonalEvalState {
    boolean isStarted();
    boolean isSelfPerfEvalDone();
    boolean isSelfCompeEvalDone();

    boolean isColleagueCompeEvalDone(String colleagueId);

    boolean isFirstPerfEvalHad();
    Grade getFirstPerfEvalGrade();
    boolean isFirstCompeEvalHad();
    Grade getFirstCompeEvalGrade();
    Grade getFirstTotalEvalGrade();
    boolean isFirstTotalEvalDone();

}

class PersonalEvalStateBuilder {
    static PersonalEvalState notStarted() {
        return new PersonalEvalState() {
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
            public boolean isColleagueCompeEvalDone(String colleagueId) {
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
        };
    }
}
