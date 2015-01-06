package net.madvirus.eval.api;

public class CreateMappingsCommand {
    private String id;
    private String evalSeasonId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public void setEvalSeasonId(String evalSeasonId) {
        this.evalSeasonId = evalSeasonId;
    }
}
