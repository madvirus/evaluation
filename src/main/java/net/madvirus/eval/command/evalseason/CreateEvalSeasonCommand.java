package net.madvirus.eval.command.evalseason;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateEvalSeasonCommand {
    @JsonProperty
    private String evalSeasonId;
    @JsonProperty
    private String name;

    public CreateEvalSeasonCommand(String evalSeasonId, String name) {
        this.evalSeasonId = evalSeasonId;
        this.name = name;
    }

    protected CreateEvalSeasonCommand() {
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getName() {
        return name;
    }
}
