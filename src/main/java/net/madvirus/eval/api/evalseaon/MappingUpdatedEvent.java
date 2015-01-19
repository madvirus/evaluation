package net.madvirus.eval.api.evalseaon;

import net.madvirus.eval.api.RateeMapping;

public class MappingUpdatedEvent extends EvalSeasonEvent {
    private String evalSeasonId;
    private RateeMapping mapping;

    MappingUpdatedEvent() {}

    public MappingUpdatedEvent(String id, RateeMapping mapping) {
        this.evalSeasonId = id;
        this.mapping = mapping;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public RateeMapping getMapping() {
        return mapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingUpdatedEvent that = (MappingUpdatedEvent) o;

        if (evalSeasonId != null ? !evalSeasonId.equals(that.evalSeasonId) : that.evalSeasonId != null) return false;
        if (mapping != null ? !mapping.equals(that.mapping) : that.mapping != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evalSeasonId != null ? evalSeasonId.hashCode() : 0;
        result = 31 * result + (mapping != null ? mapping.hashCode() : 0);
        return result;
    }
}
