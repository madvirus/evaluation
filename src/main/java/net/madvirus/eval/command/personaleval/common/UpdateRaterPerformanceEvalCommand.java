package net.madvirus.eval.command.personaleval.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.domain.personaleval.ItemEval;

import java.util.List;

public abstract class UpdateRaterPerformanceEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String raterId;
    @JsonProperty
    private List<ItemEval> itemEvals;
    @JsonProperty
    private ItemEval totalEval;

    protected UpdateRaterPerformanceEvalCommand() {}
    public UpdateRaterPerformanceEvalCommand(String evalSeasonId, String rateeId, String raterId,
                                             List<ItemEval> itemEvals, ItemEval totalEval) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.raterId = raterId;
        this.itemEvals = itemEvals;
        this.totalEval = totalEval;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setRaterId(String raterId) {
        this.raterId = raterId;
    }

    public void setRateeId(String rateeId) {
        this.rateeId = rateeId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getRaterId() {
        return raterId;
    }

    public List<ItemEval> getItemEvals() {
        return itemEvals;
    }

    public ItemEval getTotalEval() {
        return totalEval;
    }
}
