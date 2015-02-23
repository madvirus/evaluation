package net.madvirus.eval.command.personaleval.second;

import net.madvirus.eval.command.personaleval.common.TotalEvalUpdate;
import net.madvirus.eval.command.personaleval.common.UpdateRaterTotalEvalCommand;

import java.util.List;

public class UpdateSecondTotalEvalCommand extends UpdateRaterTotalEvalCommand {
    protected UpdateSecondTotalEvalCommand() {
    }

    public UpdateSecondTotalEvalCommand(String evalSeasonId, String raterId, List<TotalEvalUpdate> evalUpdates, boolean done) {
        super(evalSeasonId, raterId, evalUpdates, done);
    }
}
