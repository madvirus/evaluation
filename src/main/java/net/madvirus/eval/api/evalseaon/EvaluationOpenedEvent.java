package net.madvirus.eval.api.evalseaon;

public class EvaluationOpenedEvent extends EvalSeasonEvent {
    private String id;

    EvaluationOpenedEvent() {}

    public EvaluationOpenedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
