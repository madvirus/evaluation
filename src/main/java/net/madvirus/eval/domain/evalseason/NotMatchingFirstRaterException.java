package net.madvirus.eval.domain.evalseason;

import static java.lang.String.format;

public class NotMatchingFirstRaterException extends RuntimeException{
    public NotMatchingFirstRaterException(String rateeId, String firstRaterId) {
        super(format("%s is not first rater for %s", firstRaterId, rateeId));
    }
}
