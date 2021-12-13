package com.tezodevi.android.model;

public class UserWebsocket {
    private final String token;
    private final String username;

    public UserWebsocket(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
