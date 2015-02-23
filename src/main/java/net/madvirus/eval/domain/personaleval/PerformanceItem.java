package net.madvirus.eval.domain.personaleval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceItem {
    @JsonProperty
    private String category;
    @JsonProperty
    private String goalType;
    @JsonProperty
    private String goal;
    @JsonProperty
    private String result;
    @JsonProperty
    private int weight;

    protected PerformanceItem() {}
    public PerformanceItem(String category, String goalType, String goal, String result, int weight) {
        this.category = category;
        this.goalType = goalType;
        this.goal = goal;
        this.result = result;
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public String getGoalType() {
        return goalType;
    }

    public String getGoal() {
        return goal;
    }

    public String getResult() {
        return result;
    }

    public int getWeight() {
        return weight;
    }
}
