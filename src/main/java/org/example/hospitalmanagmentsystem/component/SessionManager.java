package org.example.hospitalmanagmentsystem.component;

import org.example.hospitalmanagmentsystem.backend.UserAccount;

public final class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private UserAccount currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void login(UserAccount userAccount) {
        this.currentUser = userAccount;
    }

    public void logout() {
        this.currentUser = null;
    }
}
