package net.madvirus.eval.api.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateSelfPerformanceEvalCommand {
    @JsonProperty
    private String personalEvalId;
    @JsonProperty
    private String evalSeasonId;
    private String userId;
    @JsonProperty
    private List<PerformanceItemAndSelfEval> itemAndEvals;
    @JsonProperty
    private boolean done;

    protected UpdateSelfPerformanceEvalCommand() {}

    public UpdateSelfPerformanceEvalCommand(String personalEvalId, String evalSeasonId, String userId, List<PerformanceItemAndSelfEval> itemAndEvals, boolean done) {
        this.personalEvalId = personalEvalId;
        this.evalSeasonId = evalSeasonId;
        this.userId = userId;
        this.itemAndEvals = itemAndEvals;
        this.done = done;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public void setUserId(String raterId) {
        this.userId = raterId;
    }

    public String getUserId() {
        return userId;
    }

    public List<PerformanceItemAndSelfEval> getItemAndEvals() {
        return itemAndEvals;
    }

    public boolean isDone() {
        return done;
    }

    public int getWeightSum() {
        return itemAndEvals.stream()
                .mapToInt(x -> x.getItem().getWeight())
                .sum();
    }
}
