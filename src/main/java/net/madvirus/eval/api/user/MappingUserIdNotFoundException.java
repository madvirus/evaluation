package net.madvirus.eval.api.user;

import java.util.Arrays;
import java.util.List;

public class MappingUserIdNotFoundException extends RuntimeException {
    private List<String> ids;

    public MappingUserIdNotFoundException(String... ids) {
        this.ids = Arrays.asList(ids);
    }

    public List<String> getIds() {
        return ids;
    }
}
