package net.madvirus.eval.domain.evalseason;

import java.util.List;

public class IdBelongToManyRulesException extends RuntimeException {
    private List<String> rateeIds;

    public IdBelongToManyRulesException(List<String> idBelongToManyRules) {
        rateeIds = idBelongToManyRules;
    }

    public List<String> getRateeIds() {
        return rateeIds;
    }
}
