package net.madvirus.eval.api.evalseaon;

import org.axonframework.repository.AggregateNotFoundException;

public class EvalSeasonNotFoundException extends RuntimeException {
    public EvalSeasonNotFoundException(AggregateNotFoundException e) {
        super(e);
    }
    public EvalSeasonNotFoundException() {
    }
}
