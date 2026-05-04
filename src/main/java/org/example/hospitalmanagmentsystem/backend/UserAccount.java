package org.example.hospitalmanagmentsystem.backend;

public class UserAccount {
    private final String username;
    private final String passwordHash;
    private final Role role;
    private final String linkedId;
    private boolean active;

    public UserAccount(String username, String passwordHash, Role role, String linkedId, boolean active) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.linkedId = linkedId;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
