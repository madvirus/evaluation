package net.madvirus.eval.api.personaleval.first;

public class RejectSelfCompetencyEvalCommand {
    private String evalSeasonId;
    private String rateeId;
    private String raterId;

    protected RejectSelfCompetencyEvalCommand() {}
    public RejectSelfCompetencyEvalCommand(String evalSeasonId, String rateeId, String raterId) {
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
