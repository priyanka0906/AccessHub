package com.priyanka.accesshub.dto.internal;


public class UserPrincipal {
    private final String username;
    private final String clientId;

    public UserPrincipal(String username, String clientId) {
        this.username = username;
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }
}

