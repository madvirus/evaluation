package net.madvirus.eval.api;

public class MappingsCreatedEvent {
    private String mappingInfoId;
    private String evalSeasonId;

    public MappingsCreatedEvent(String mappingInfoId, String evalSeasonId) {
        this.mappingInfoId = mappingInfoId;
        this.evalSeasonId = evalSeasonId;
    }

    public String getMappingInfoId() {
        return mappingInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingsCreatedEvent that = (MappingsCreatedEvent) o;

        if (!mappingInfoId.equals(that.mappingInfoId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mappingInfoId.hashCode();
    }
}
