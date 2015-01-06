package net.madvirus.eval.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RateeMapping {
    private String rateeId;
    private String type;
    private String firstRaterId;
    private String secondRaterId;
    private List<String> colleagueRaterIds;

    public RateeMapping(String rateeId, String type, String firstRaterId, String secondRaterId, String ... colleagueRaterIds) {
        this.rateeId = rateeId;
        this.type = type;
        this.firstRaterId = firstRaterId;
        this.secondRaterId = secondRaterId;
        this.colleagueRaterIds = Arrays.asList(colleagueRaterIds);
    }

    public String getRateeId() {
        return rateeId;
    }

    public String getType() {
        return type;
    }

    public String getFirstRaterId() {
        return firstRaterId;
    }

    public String getSecondRaterId() {
        return secondRaterId;
    }

    public List<String> getColleagueRaterIds() {
        return Collections.unmodifiableList(colleagueRaterIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RateeMapping that = (RateeMapping) o;


        if (colleagueRaterIds != null ? !colleagueRaterIds.equals(that.colleagueRaterIds) : that.colleagueRaterIds != null)
            return false;
        if (firstRaterId != null ? !firstRaterId.equals(that.firstRaterId) : that.firstRaterId != null) return false;
        if (rateeId != null ? !rateeId.equals(that.rateeId) : that.rateeId != null) return false;
        if (secondRaterId != null ? !secondRaterId.equals(that.secondRaterId) : that.secondRaterId != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

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
