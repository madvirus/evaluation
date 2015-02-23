package net.madvirus.eval.domain.personaleval;

import java.util.Collection;
import java.util.Map;

public class AllCompeEvals {
    private CompetencyEvalSet selfEvalSet;
    private Map<String, CompetencyEvalSet> colleagueEvals;
    private CompetencyEvalSet firstEvalSet;
    private CompetencyEvalSet secondEvalSet;

    public AllCompeEvals(CompetencyEvalSet selfEvalSet, Map<String, CompetencyEvalSet> colleagueEvals, CompetencyEvalSet firstEvalSet, CompetencyEvalSet secondEvalSet) {
        this.selfEvalSet = selfEvalSet;
        this.colleagueEvals = colleagueEvals;
        this.firstEvalSet = firstEvalSet;
        this.secondEvalSet = secondEvalSet;
    }

    public CompetencyEvalSet getSelfEvalSet() {
        return selfEvalSet;
    }

    public Collection<CompetencyEvalSet> getColleagueEvals() {
        return colleagueEvals.values();
    }

    public Double getColleagueCommonGradeAvg(int idx) {
        int sum = 0;
        int count = 0;
        for (CompetencyEvalSet evalSet : colleagueEvals.values()) {
            if (evalSet.isDone()) {
                sum += evalSet.getCommonsEvals().get(idx).getGrade().getNumber();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return (double) sum / count;
    }

    public Double getColleagueLeadershipGradeAvg(int idx) {
        int sum = 0;
        int count = 0;
        for (CompetencyEvalSet evalSet : colleagueEvals.values()) {
            if (evalSet.isDone()) {
                sum += evalSet.getLeadershipEvals().get(idx).getGrade().getNumber();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return (double) sum / count;
    }

    public Double getColleagueAmGradeAvg(int idx) {
        int sum = 0;
        int count = 0;
        for (CompetencyEvalSet evalSet : colleagueEvals.values()) {
            if (evalSet.isDone()) {
                sum += evalSet.getAmEvals().get(idx).getGrade().getNumber();
                count++;
            }
        }
        if (count == 0) {
            return null;
        }
        return (double) sum / count;
    }

    public CompetencyEvalSet getFirstEvalSet() {
        return firstEvalSet;
    }

    public CompetencyEvalSet getSecondEvalSet() {
        return secondEvalSet;
    }

}
