package net.madvirus.eval.api.evalseaon;

import java.util.List;

public class SomeColleagueEvalDoneException extends CanNotUpdateMappingException {
    private List<String> canNotUpdateIds;

    public SomeColleagueEvalDoneException(List<String> canNotUpdateIds) {
        this.canNotUpdateIds = canNotUpdateIds;
    }

    public List<String> getCanNotUpdateIds() {
        return canNotUpdateIds;
    }

}
