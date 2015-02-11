package net.madvirus.eval.api.evalseaon;

public class ColleagueEvalStartedEvent {
    private String evalSeasonId;

    public ColleagueEvalStartedEvent(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }

    protected ColleagueEvalStartedEvent() {
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }
}
