package net.madvirus.eval.command.personaleval.second;

import net.madvirus.eval.command.personaleval.common.UpdateRaterPerformanceEvalCommand;
import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;

public class UpdateSecondPerformanceEvalCommand extends UpdateRaterPerformanceEvalCommand {
    protected UpdateSecondPerformanceEvalCommand() {
    }

    public UpdateSecondPerformanceEvalCommand(String evalSeasonId, String rateeId, String raterId, List<ItemEval> itemEvals, ItemEval totalEval) {
        super(evalSeasonId, rateeId, raterId, itemEvals, totalEval);
    }
}
