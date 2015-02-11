package net.madvirus.eval.api.personaleval;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CompetencyEvaluation {
    private Optional<CompetencyEvalSet> selfEvalSet;
    private Map<String, CompetencyEvalSet> colleagueEvals = new HashMap<>();
    private Optional<CompetencyEvalSet> firstEvalSet;

    public CompetencyEvaluation() {
        selfEvalSet = Optional.empty();
        firstEvalSet = Optional.empty();
    }

    public void updateSelfEval(CompetencyEvalSet selfEvalSet) {
        this.selfEvalSet = Optional.of(selfEvalSet);
    }

    public Optional<CompetencyEvalSet> getSelfEvalSet() {
        return selfEvalSet;
    }

    public Optional<CompetencyEvalSet> getColleagueEvalOf(String colleagueId) {
        return Optional.ofNullable(colleagueEvals.get(colleagueId));
    }

    public void updateColleagueCompetencyEval(String colleagueRaterId, CompetencyEvalSet evalSet) {
        if (colleagueEvals.containsKey(colleagueRaterId)) {
            CompetencyEvalSet colleagueEvalSet = colleagueEvals.get(colleagueRaterId);
            if (colleagueEvalSet.isDone()) {
                throw new AlreadyEvaluationDoneException();
            }
        }
        colleagueEvals.put(colleagueRaterId, evalSet);
    }

    public AllCompeEvals getAllCompeEvals() {
        return new AllCompeEvals(
                selfEvalSet.isPresent() ? selfEvalSet.get() : null,
                colleagueEvals,
                firstEvalSet.isPresent() ? firstEvalSet.get() : null);
    }

    public void updateFirstCompeEval(CompetencyEvalSet evalSet) {
        firstEvalSet = Optional.of(evalSet);
    }

    public Optional<CompetencyEvalSet> getFirstEvalSet() {
        return firstEvalSet;
    }

    public void rejectSelfEval() {
        CompetencyEvalSet oldEvalSet = selfEvalSet.get();
        selfEvalSet = Optional.of(oldEvalSet.copy(false));
        firstEvalSet = Optional.empty();
    }

    public void updateFirstCompeEvalDone() {
        firstEvalSet = Optional.of(firstEvalSet.get().copy(true));
    }
}
