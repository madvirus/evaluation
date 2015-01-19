package net.madvirus.eval.api.evalseaon;

import java.util.Date;

public class EvalSeasonCreatedEvent extends EvalSeasonEvent {
    private String evalSeasonId;
    private String name;
    private Date creationDate;

    EvalSeasonCreatedEvent() {
    }

    public EvalSeasonCreatedEvent(String evalseasonId, String name, Date creationDate) {
        this.evalSeasonId = evalseasonId;
        this.name = name;
        this.creationDate = creationDate;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvalSeasonCreatedEvent that = (EvalSeasonCreatedEvent) o;

        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null)
            return false;
        if (evalSeasonId != null ? !evalSeasonId.equals(that.evalSeasonId) : that.evalSeasonId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evalSeasonId != null ? evalSeasonId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}
