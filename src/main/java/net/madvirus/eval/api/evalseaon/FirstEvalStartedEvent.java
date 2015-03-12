package net.madvirus.eval.api.evalseaon;

public class FirstEvalStartedEvent {
    private String evalSeasonId;

    public FirstEvalStartedEvent(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    protected FirstEvalStartedEvent() {
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }
}
