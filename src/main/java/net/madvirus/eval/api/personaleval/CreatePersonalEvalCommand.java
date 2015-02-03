package net.madvirus.eval.api.personaleval;

public class CreatePersonalEvalCommand {
    private final String evalSeasonId;
    private final String userId;

    public CreatePersonalEvalCommand(String evalSeasonId, String userId) {
        this.evalSeasonId = evalSeasonId;
        this.userId = userId;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getUserId() {
        return userId;
    }
}
