package net.madvirus.eval.system.security;

public class Authority {
    private String userId;
    private String role;

    public Authority(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
