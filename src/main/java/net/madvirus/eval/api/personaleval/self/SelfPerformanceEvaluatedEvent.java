package net.madvirus.eval.api.personaleval.self;

import net.madvirus.eval.domain.personaleval.PerformanceItemAndSelfEval;

import java.util.List;

public class SelfPerformanceEvaluatedEvent {
    private String personalEvalId;
    private boolean done;
    private List<PerformanceItemAndSelfEval> performanceItemAndSelfEval;

    protected SelfPerformanceEvaluatedEvent() {}

    public SelfPerformanceEvaluatedEvent(String personalEvalId, boolean done, List<PerformanceItemAndSelfEval> performanceItemAndSelfEval) {
        this.personalEvalId = personalEvalId;
        this.done = done;
        this.performanceItemAndSelfEval = performanceItemAndSelfEval;
    }

    public boolean isDone() {
        return done;
    }

    public List<PerformanceItemAndSelfEval> getPerformanceItemAndSelfEval() {
        return performanceItemAndSelfEval;
    }

    public String getPersonalEvalId() {
        return personalEvalId;
    }
}
