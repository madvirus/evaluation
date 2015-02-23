package net.madvirus.eval.command.personaleval.second;

import net.madvirus.eval.command.personaleval.common.UpdateRaterCompetencyEvalCommand;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class UpdateSecondCompetencyEvalCommand extends UpdateRaterCompetencyEvalCommand {
    protected UpdateSecondCompetencyEvalCommand() {
    }

    public UpdateSecondCompetencyEvalCommand(String evalSeasonId, String rateeId, String raterId, CompetencyEvalSet evalSet) {
        super(evalSeasonId, rateeId, raterId, evalSet);
    }
}
