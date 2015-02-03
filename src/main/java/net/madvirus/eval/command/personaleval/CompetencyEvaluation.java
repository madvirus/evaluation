package net.madvirus.eval.command.personaleval;

import net.madvirus.eval.api.personaleval.CompetencyEvalSet;
import net.madvirus.eval.api.personaleval.ItemEval;

import java.util.List;

public class CompetencyEvaluation {
    private boolean selfEvalDone;
    private CompetencyEvalSet selfEvalSet;

    public void updateSelfEval(CompetencyEvalSet selfEvalSet, boolean selfEvalDone) {
        this.selfEvalSet = selfEvalSet;
        this.selfEvalDone = selfEvalDone;
    }

    public boolean isSelfEvalDone() {
        return selfEvalDone;
    }

    public CompetencyEvalSet getSelfEvalSet() {
        return selfEvalSet;
    }
}
