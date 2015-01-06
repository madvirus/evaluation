package net.madvirus.eval.api;

public class EvalSeasonCreatedEvent {
    private String evalSeasonId;
    private String name;

    public EvalSeasonCreatedEvent(String evalseasonId, String name) {
        this.evalSeasonId = evalseasonId;
        this.name = name;
    }

    public String getEvalSeasonId() {
        return evalSeasonId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvalSeasonCreatedEvent that = (EvalSeasonCreatedEvent) o;

        if (evalSeasonId != null ? !evalSeasonId.equals(that.evalSeasonId) : that.evalSeasonId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evalSeasonId != null ? evalSeasonId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
