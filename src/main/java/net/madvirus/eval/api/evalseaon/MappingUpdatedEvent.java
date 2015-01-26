package net.madvirus.eval.api.evalseaon;

import net.madvirus.eval.api.RateeMapping;

import java.util.List;

public class MappingUpdatedEvent extends EvalSeasonEvent {
    private String evalSeasonId;
    private List<RateeMapping> mappings;

    MappingUpdatedEvent() {}

    public MappingUpdatedEvent(String id, List<RateeMapping> mappings) {
        this.evalSeasonId = id;
        this.mappings = mappings;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public List<RateeMapping> getMappings() {
        return mappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingUpdatedEvent that = (MappingUpdatedEvent) o;

        if (evalSeasonId != null ? !evalSeasonId.equals(that.evalSeasonId) : that.evalSeasonId != null) return false;
        if (mappings != null ? !mappings.equals(that.mappings) : that.mappings != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evalSeasonId != null ? evalSeasonId.hashCode() : 0;
        result = 31 * result + (mappings != null ? mappings.hashCode() : 0);
        return result;
    }
}
