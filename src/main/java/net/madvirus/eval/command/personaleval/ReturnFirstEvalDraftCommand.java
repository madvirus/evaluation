package net.madvirus.eval.command.personaleval;

public class ReturnFirstEvalDraftCommand {

    private String evalSeasonId;
    private String firstRaterId;

    protected ReturnFirstEvalDraftCommand() {}
    public ReturnFirstEvalDraftCommand(String evalSeasonId, String firstRaterId) {
        this.evalSeasonId = evalSeasonId;
        this.firstRaterId = firstRaterId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }
}
