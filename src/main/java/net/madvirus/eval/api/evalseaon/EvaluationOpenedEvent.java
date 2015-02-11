package net.madvirus.eval.api.evalseaon;

public class EvaluationOpenedEvent extends EvalSeasonEvent {
    private String evalSeasonId;

    EvaluationOpenedEvent() {}

    public EvaluationOpenedEvent(String id) {
        this.evalSeasonId = id;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }
}
