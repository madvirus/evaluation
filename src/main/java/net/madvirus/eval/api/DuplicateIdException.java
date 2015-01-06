package net.madvirus.eval.api;

public class DuplicateIdException extends RuntimeException {
    private Object id;

    public DuplicateIdException(Object id) {
        super(String.format("%s is duplicate Id", id));
        this.id = id;
    }

    public Object getId() {
        return id;
    }
}
