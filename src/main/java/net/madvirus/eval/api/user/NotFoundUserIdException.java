package net.madvirus.eval.api.user;

import java.util.Arrays;
import java.util.List;

public class NotFoundUserIdException extends RuntimeException {
    private List<String> ids;

    public NotFoundUserIdException(String... ids) {
        this.ids = Arrays.asList(ids);
    }

    public List<String> getIds() {
        return ids;
    }
}
