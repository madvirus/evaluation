package net.madvirus.eval.command.personaleval.first;

import net.madvirus.eval.command.personaleval.common.TotalEvalUpdate;
import net.madvirus.eval.command.personaleval.common.UpdateRaterTotalEvalCommand;

import java.util.List;

public class UpdateFirstTotalEvalCommand extends UpdateRaterTotalEvalCommand {
    protected UpdateFirstTotalEvalCommand() {
    }

    public UpdateFirstTotalEvalCommand(String evalSeasonId, String raterId, List<TotalEvalUpdate> evalUpdates, boolean done) {
        super(evalSeasonId, raterId, evalUpdates, done);
    }
}
