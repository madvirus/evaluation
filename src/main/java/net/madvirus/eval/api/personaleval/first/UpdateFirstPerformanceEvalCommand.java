package net.madvirus.eval.api.personaleval.first;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.madvirus.eval.api.personaleval.ItemEval;

import java.util.List;

public class UpdateFirstPerformanceEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String firstRaterId;
    @JsonProperty
    private List<ItemEval> itemEvals;
    @JsonProperty
    private ItemEval totalEval;

    protected UpdateFirstPerformanceEvalCommand() {}
    public UpdateFirstPerformanceEvalCommand(String evalSeasonId, String rateeId, String firstRaterId,
                                             List<ItemEval> itemEvals, ItemEval totalEval) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.firstRaterId = firstRaterId;
        this.itemEvals = itemEvals;
        this.totalEval = totalEval;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    public void setFirstRaterId(String firstRaterId) {
        this.firstRaterId = firstRaterId;
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

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public List<ItemEval> getItemEvals() {
        return itemEvals;
    }

    public ItemEval getTotalEval() {
        return totalEval;
    }
}
