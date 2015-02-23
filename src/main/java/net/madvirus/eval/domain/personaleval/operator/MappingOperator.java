package net.madvirus.eval.domain.personaleval.operator;

import net.madvirus.eval.api.RateeMapping;
import net.madvirus.eval.api.evalseaon.FirstEvalDoneException;
import net.madvirus.eval.api.personaleval.ColleagueEvalDeletedEvent;
import net.madvirus.eval.api.personaleval.PersonalEvalFirstRaterChangedEvent;
import net.madvirus.eval.api.personaleval.PersonalEvalSecondRaterChangedEvent;
import net.madvirus.eval.domain.personaleval.*;

import java.util.List;
import java.util.Set;

public class MappingOperator {
    private final CompetencyEvaluation compeEval;
    private PersonalEval personalEval;

    public MappingOperator(PersonalEval personalEval, CompetencyEvaluation compeEval) {
        this.personalEval = personalEval;
        this.compeEval = compeEval;
    }

    public void applyUpdatedMapping(RateeMapping rateeMapping) {
        boolean[] changed = checkChanged(rateeMapping);
        boolean firstChanged = changed[0];
        boolean secondChanged = changed[1];
        boolean colleagueChanged = changed[2];
        if (!firstChanged && !secondChanged && !colleagueChanged) return;

        if (firstChanged) {
            if (personalEval.isFirstCompeEvalHad() || personalEval.isFirstPerfEvalHad()) {
                throw new FirstEvalStartedException();
            }
            personalEval.applyEvent(
                    new PersonalEvalFirstRaterChangedEvent(
                            personalEval.getId(),
                            personalEval.getFirstRaterId(),
                            rateeMapping.getFirstRaterId()));
        }
        if (secondChanged) {
            if (personalEval.isSecondCompeEvalHad() || personalEval.isSecondPerfEvalHad()) {
                throw new SecondEvalStartedException();
            }
            personalEval.applyEvent(
                    new PersonalEvalSecondRaterChangedEvent(
                            personalEval.getId(),
                            personalEval.getSecondRaterId(),
                            rateeMapping.getSecondRaterId()));
        }

        if (personalEval.isFirstTotalEvalDone()) {
            throw new FirstEvalDoneException();
        }
        if (personalEval.isSecondTotalEvalDone()) {
            throw new SecondEvalDoneException();
        }
        if (colleagueChanged) {
            Set<String> newColleagueRaterIds = rateeMapping.getColleagueRaterIds();
            List<String> removeTargetColleagueIds = compeEval.removeColleagueRaterNotIn(newColleagueRaterIds);
            if (!removeTargetColleagueIds.isEmpty()) {
                personalEval.applyEvent(new ColleagueEvalDeletedEvent(personalEval.getId(), removeTargetColleagueIds));
            }
        }
    }

    private boolean[] checkChanged(RateeMapping rateeMapping) {
        boolean firstChanged = isDiffrent(rateeMapping.getFirstRaterId(), personalEval.getFirstRaterId());
        boolean secondChanged = isDiffrent(rateeMapping.getSecondRaterId(), personalEval.getSecondRaterId());
        Set<String> newCollRaterIds = rateeMapping.getColleagueRaterIds();
        boolean colleagueRemoved = compeEval.checkColleagueRemoved(newCollRaterIds);
        boolean colleagueAdded = compeEval.checkColleagueAdded(newCollRaterIds);
        return new boolean[]{firstChanged, secondChanged, colleagueRemoved || colleagueAdded};
    }

    private boolean isDiffrent(String newFirstId, String oldFirstId) {
        if (newFirstId != null && oldFirstId == null) {
            return true;
        }
        if (newFirstId == null && oldFirstId != null) {
            return true;
        }
        if (newFirstId != null && oldFirstId != null && !newFirstId.equals(oldFirstId)) {
            return true;
        }
        return false;
    }
}
