package net.madvirus.eval.web.dataloader;

public interface PersonalEvalState {
    boolean isStarted();
    boolean isSelfPerfEvalDone();
    boolean isSelfCompeEvalDone();
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
        };
    }
}
