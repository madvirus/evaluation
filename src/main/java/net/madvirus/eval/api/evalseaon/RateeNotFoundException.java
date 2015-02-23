package net.madvirus.eval.api.evalseaon;

public class RateeNotFoundException extends RuntimeException {
    private final String rateeId;

    public RateeNotFoundException(String rateeId) {
        super(String.format("Ratee %s is not found", rateeId));
        this.rateeId = rateeId;
    }

    public String getRateeId() {
        return rateeId;
    }
}
