package net.madvirus.eval.api;

import net.madvirus.eval.api.evalseaon.RateeType;

import java.util.*;

public class RateeMapping {
    private String rateeId;
    private RateeType type;
    private String firstRaterId;
    private String secondRaterId;
    private Set<String> colleagueRaterIds;

    protected RateeMapping() {
    }

    public RateeMapping(String rateeId, RateeType type, String firstRaterId, String secondRaterId, String... colleagueRaterIds) {
        this.rateeId = rateeId;
        this.type = type;
        this.firstRaterId = firstRaterId;
        this.secondRaterId = secondRaterId;
        this.colleagueRaterIds = new HashSet<>(Arrays.asList(colleagueRaterIds));
    }

    public String getRateeId() {
        return rateeId;
    }

    public RateeType getType() {
        return type;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public boolean hasFirstRater() {
        return firstRaterId != null;
    }

    public String getSecondRaterId() {
        return secondRaterId;
    }

    public Set<String> getColleagueRaterIds() {
        if (colleagueRaterIds == null)
            return Collections.emptySet();
        else
            return Collections.unmodifiableSet(colleagueRaterIds);
    }

    @Override
    public String toString() {
        return "RateeMapping{" +
                "rateeId='" + rateeId + '\'' +
                ", type=" + type +
                ", firstRaterId='" + firstRaterId + '\'' +
                ", secondRaterId='" + secondRaterId + '\'' +
                ", colleagueRaterIds=" + colleagueRaterIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateeMapping that = (RateeMapping) o;

        if (rateeId != null ? !rateeId.equals(that.rateeId) : that.rateeId != null) return false;
        if (type != that.type) return false;

        if (colleagueRaterIds != null ? !colleagueRaterIds.equals(that.colleagueRaterIds) : that.colleagueRaterIds != null)
            return false;

        if (firstRaterId != null || that.firstRaterId != null)
            if (firstRaterId != null ? !firstRaterId.equals(that.firstRaterId) : that.firstRaterId != null)
                return false;

        if (secondRaterId != null ? !secondRaterId.equals(that.secondRaterId) : that.secondRaterId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rateeId != null ? rateeId.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (firstRaterId != null ? firstRaterId.hashCode() : 0);
        result = 31 * result + (secondRaterId != null ? secondRaterId.hashCode() : 0);
        result = 31 * result + (colleagueRaterIds != null ? colleagueRaterIds.hashCode() : 0);
        return result;
    }
}
