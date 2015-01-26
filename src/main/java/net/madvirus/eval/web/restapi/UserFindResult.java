package net.madvirus.eval.web.restapi;

public class UserFindResult {
    private String name;
    private boolean found;
    private String id;

    public UserFindResult(String name, boolean found, String id) {
        this.name = name;
        this.found = found;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isFound() {
        return found;
    }

    public String getId() {
        return id;
    }

    public static UserFindResult found(String name, String id) {
        return new UserFindResult(name, true, id);
    }
    public static UserFindResult notFound(String name) {
        return new UserFindResult(name, false, null);
    }
}
