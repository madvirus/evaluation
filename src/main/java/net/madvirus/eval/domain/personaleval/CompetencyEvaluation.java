package net.madvirus.eval.domain.personaleval;

import net.madvirus.eval.api.evalseaon.SomeColleagueEvalDoneException;
import net.madvirus.eval.api.personaleval.AlreadyEvaluationDoneException;

import java.util.*;

public class CompetencyEvaluation {
    private Optional<CompetencyEvalSet> selfEvalSet;
    private Map<String, CompetencyEvalSet> colleagueEvals = new HashMap<>();
    private Optional<CompetencyEvalSet> firstEvalSet;
    private Optional<CompetencyEvalSet> secondEvalSet;

    public CompetencyEvaluation() {
        selfEvalSet = Optional.empty();
        firstEvalSet = Optional.empty();
        secondEvalSet = Optional.empty();
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
        colleagueEvals.put(colleagueRaterId, evalSet);
    }

    public AllCompeEvals getAllCompeEvals() {
        return new AllCompeEvals(
                selfEvalSet.orElse(null),
                colleagueEvals,
                firstEvalSet.orElse(null),
                secondEvalSet.orElse(null));
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


    public Optional<CompetencyEvalSet> getSecondEvalSet() {
        return secondEvalSet;
    }

    public void updateSecondCompeEval(CompetencyEvalSet evalSet) {
        secondEvalSet = Optional.of(evalSet);
    }

    public void updateSecondCompeEvalDone() {
        secondEvalSet = Optional.of(secondEvalSet.get().copy(true));
    }

    public List<String> removeColleagueRaterNotIn(Set<String> newColleagueRaterIds) {
        Set<String> raterIdNotInNewMapping = diff(colleagueEvals.keySet(), newColleagueRaterIds);
        List<String> removeTargetIds = new ArrayList<>();
        List<String> canNotRemoveIds = new ArrayList<>();
        for (String id : raterIdNotInNewMapping) {
            CompetencyEvalSet evalSet = colleagueEvals.get(id);
            if (evalSet != null && !evalSet.isDone()) {
                removeTargetIds.add(id);
            } else if (evalSet != null && evalSet.isDone()) {
                canNotRemoveIds.add(id);
            }
        }
        if (!canNotRemoveIds.isEmpty()) {
            throw new SomeColleagueEvalDoneException(canNotRemoveIds);
        }
        return removeTargetIds;
    }

    private Set<String> diff(Set<String> oldIds, Set<String> newIds) {
        Set<String> notInNew = new HashSet<>();
        for (String id : oldIds) {
            if (!newIds.contains(id))
                notInNew.add(id);
        }
        return notInNew;
    }

    public boolean checkColleagueRemoved(Set<String> newCollRaterIds) {
        return !diff(colleagueEvals.keySet(), newCollRaterIds).isEmpty();
    }

    public void removeColleagueRaterEval(List<String> colleagueIds) {
        colleagueIds.forEach(id -> colleagueEvals.remove(id));
    }

    public boolean checkColleagueAdded(Set<String> newCollRaterIds) {
        return !diff(newCollRaterIds, colleagueEvals.keySet()).isEmpty();
    }

    public void returnFirstDraft() {
        if (firstEvalSet.isPresent()) {
            CompetencyEvalSet evalSet = firstEvalSet.get().copy(false);
            firstEvalSet = Optional.of(evalSet);
        }
    }
}
