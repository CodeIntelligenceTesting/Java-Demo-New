package com.demo.security.helper;

public class JsonAddBody {
    private UserJSONObject authenticatedAdminUser;
    private UserJSONObject newUser;

    public UserJSONObject getAuthenticatedAdminUser() {
        return authenticatedAdminUser;
    }

    public void setAuthenticatedAdminUser(UserJSONObject authenticatedAdminUser) {
        this.authenticatedAdminUser = authenticatedAdminUser;
    }

    public UserJSONObject getNewUser() {
        return newUser;
    }

    public void setNewUser(UserJSONObject newUser) {
        this.newUser = newUser;
    }
}
