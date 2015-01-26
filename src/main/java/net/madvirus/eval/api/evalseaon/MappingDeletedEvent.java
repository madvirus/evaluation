package net.madvirus.eval.api.evalseaon;

import java.util.List;

public class MappingDeletedEvent extends EvalSeasonEvent {
    private String evalSeasonId;
    private List<String> deletedRateeIds;

    MappingDeletedEvent() {}

    public MappingDeletedEvent(String evalSeasonId, List<String> rateeIds) {
        this.evalSeasonId = evalSeasonId;
        this.deletedRateeIds = rateeIds;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public List<String> getDeletedRateeIds() {
        return deletedRateeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingDeletedEvent that = (MappingDeletedEvent) o;

        if (deletedRateeIds != null ? !deletedRateeIds.equals(that.deletedRateeIds) : that.deletedRateeIds != null)
            return false;
        if (evalSeasonId != null ? !evalSeasonId.equals(that.evalSeasonId) : that.evalSeasonId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evalSeasonId != null ? evalSeasonId.hashCode() : 0;
        result = 31 * result + (deletedRateeIds != null ? deletedRateeIds.hashCode() : 0);
        return result;
    }
}
