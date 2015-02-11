package net.madvirus.eval.api.personaleval.first;

import net.madvirus.eval.api.personaleval.PersonalEval;
import net.madvirus.eval.api.personaleval.SelfEvalNotYetFinishedException;

public class FirstRaterOperator {

    private PersonalEval personalEval;

    public FirstRaterOperator(PersonalEval personalEval) {
        this.personalEval = personalEval;
    }

    public void updateFirstPerformanceEval(UpdateFirstPerformanceEvalCommand command) {
        checkFirstRaterAndThrowIfNot(command.getFirstRaterId());
        checkSelfPerfDoneAndThrowIfNot();
        personalEval.applyEvent(new FirstPerformanceEvaluatedEvent(
                personalEval.getId(), command.getItemEvals(), command.getTotalEval()));
    }

    private void checkFirstRaterAndThrowIfNot(String firstRaterId1) {
        if (!personalEval.checkFirstRater(firstRaterId1)) {
            throw new YouAreNotFirstRaterException();
        }
    }

    private void checkSelfPerfDoneAndThrowIfNot() {
        if (!personalEval.isSelfPerfEvalDone()) {
            throw new SelfEvalNotYetFinishedException();
        }
    }

    public void updateFirstCompetencyEval(UpdateFirstCompetencyEvalCommand command) {
        checkFirstRaterAndThrowIfNot(command.getFirstRaterId());
        checkSelfCompeDoneAndThrowIfNot();
        personalEval.applyEvent(new FirstCompetencyEvaluatedEvent(personalEval.getId(), command.getFirstRaterId(), command.getEvalSet()));

    }

    private void checkSelfCompeDoneAndThrowIfNot() {
        if (!personalEval.isSelfCompeEvalDone()) {
            throw new SelfEvalNotYetFinishedException();
        }
    }

    public void rejectSelfPerformanceEval(RejectSelfPerformanceEvalCommand command) {
        checkFirstRaterAndThrowIfNot(command.getRaterId());
        checkSelfPerfDoneAndThrowIfNot();
        personalEval.applyEvent(new SelfPerformanceEvalRejectedEvent(personalEval.getId(), command.getRateeId(), command.getRaterId()));
    }

    public void rejectSelfCompetencyEval(RejectSelfCompetencyEvalCommand command) {
        checkFirstRaterAndThrowIfNot(command.getRaterId());
        checkSelfCompeDoneAndThrowIfNot();
        personalEval.applyEvent(new SelfCompetencyEvalRejectedEvent(personalEval.getId(), command.getRateeId(), command.getRaterId()));
    }

}
