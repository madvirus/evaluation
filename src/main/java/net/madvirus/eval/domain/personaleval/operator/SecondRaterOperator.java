package net.madvirus.eval.domain.personaleval.operator;

import net.madvirus.eval.api.personaleval.FirstEvalNotYetFinishedException;
import net.madvirus.eval.api.personaleval.second.SecondCompetencyEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondPerformanceEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.SecondTotalEvaluatedEvent;
import net.madvirus.eval.api.personaleval.second.YouAreNotSecondRaterException;
import net.madvirus.eval.command.personaleval.common.UpdateRaterCompetencyEvalCommand;
import net.madvirus.eval.command.personaleval.common.UpdateRaterPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.PersonalEval;
import net.madvirus.eval.domain.personaleval.TotalEval;

public class SecondRaterOperator {
    private PersonalEval personalEval;

    public SecondRaterOperator(PersonalEval personalEval) {
        this.personalEval = personalEval;
    }

    public void updateSecondPerformanceEval(UpdateRaterPerformanceEvalCommand command) {
        checkSecondRaterAndThrowIfNot(command.getRaterId());
        checkFirstEvalDoneAndThrowIfNot();
        personalEval.applyEvent(new SecondPerformanceEvaluatedEvent(
                personalEval.getId(), command.getItemEvals(), command.getTotalEval()));
    }

    private void checkSecondRaterAndThrowIfNot(String secondRaterId1) {
        if (!personalEval.checkSecondRater(secondRaterId1)) {
            throw new YouAreNotSecondRaterException();
        }
    }

    private void checkFirstEvalDoneAndThrowIfNot() {
        if (!personalEval.isFirstTotalEvalDone()) {
            throw new FirstEvalNotYetFinishedException();
        }
    }

    public void updateSecondCompetencyEval(UpdateRaterCompetencyEvalCommand command) {
        checkSecondRaterAndThrowIfNot(command.getRaterId());
        checkFirstEvalDoneAndThrowIfNot();
        personalEval.applyEvent(new SecondCompetencyEvaluatedEvent(personalEval.getId(), command.getRaterId(), command.getEvalSet()));

    }


    public void updateSecondTotalEval(TotalEval totalEval) {
        personalEval.applyEvent(new SecondTotalEvaluatedEvent(personalEval.getId(), totalEval));
    }
}
