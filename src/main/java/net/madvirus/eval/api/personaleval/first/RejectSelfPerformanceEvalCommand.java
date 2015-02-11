package net.madvirus.eval.api.personaleval.first;

public class RejectSelfPerformanceEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String raterId;

    protected RejectSelfPerformanceEvalCommand() {}
    public RejectSelfPerformanceEvalCommand(String evalSeasonId, String rateeId, String raterId) {
        this.evalSeasonId = evalSeasonId;
        this.rateeId = rateeId;
        this.raterId = raterId;
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
}
