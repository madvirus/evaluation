package net.madvirus.eval.api;

public class EvaluationOpenedEvent {
    private String id;

    public EvaluationOpenedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
