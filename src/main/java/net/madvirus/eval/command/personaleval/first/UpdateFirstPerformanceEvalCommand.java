package net.madvirus.eval.command.personaleval.first;

import net.madvirus.eval.command.personaleval.common.UpdateRaterPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;

public class UpdateFirstPerformanceEvalCommand extends UpdateRaterPerformanceEvalCommand {
    protected UpdateFirstPerformanceEvalCommand() {
    }

    public UpdateFirstPerformanceEvalCommand(String evalSeasonId, String rateeId, String raterId, List<ItemEval> itemEvals, ItemEval totalEval) {
        super(evalSeasonId, rateeId, raterId, itemEvals, totalEval);
    }
}
