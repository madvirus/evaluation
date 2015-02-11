package net.madvirus.eval.api.personaleval.self;

import net.madvirus.eval.api.personaleval.InvalidWeightSumException;
import net.madvirus.eval.api.personaleval.PersonalEval;

public class SelfRaterOperator {
    private PersonalEval personalEval;

    public SelfRaterOperator(PersonalEval personalEval) {
        this.personalEval = personalEval;
    }

    public void updateSelfPerfomanceEvaluation(UpdateSelfPerformanceEvalCommand cmd) {
        if (cmd.isDone()) {
            if (cmd.getWeightSum() != 100) {
                throw new InvalidWeightSumException();
            }
        }
        personalEval.applyEvent(new SelfPerformanceEvaluatedEvent(personalEval.getId(), cmd.isDone(), cmd.getItemAndEvals()));
    }

    public void updateSelfCompetencyEvaluation(UpdateSelfCompetencyEvalCommand cmd) {
        // TODO 역량 평가 타입 체크
        personalEval.applyEvent(new SelfCompetencyEvaluatedEvent(personalEval.getId(), cmd.getEvalSet()));
    }
}
