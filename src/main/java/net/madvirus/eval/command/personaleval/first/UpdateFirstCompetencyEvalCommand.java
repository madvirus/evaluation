package net.madvirus.eval.command.personaleval.first;

import net.madvirus.eval.command.personaleval.common.UpdateRaterCompetencyEvalCommand;
import net.madvirus.eval.domain.personaleval.CompetencyEvalSet;

public class UpdateFirstCompetencyEvalCommand extends UpdateRaterCompetencyEvalCommand {
    protected UpdateFirstCompetencyEvalCommand() {
    }

    public UpdateFirstCompetencyEvalCommand(String evalSeasonId, String rateeId, String raterId, CompetencyEvalSet evalSet) {
        super(evalSeasonId, rateeId, raterId, evalSet);
    }
}
