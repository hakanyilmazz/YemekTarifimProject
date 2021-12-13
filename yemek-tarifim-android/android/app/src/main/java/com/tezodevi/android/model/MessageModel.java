package com.tezodevi.android.model;

public class MessageModel {
    private final String username;
    private final String photo;
    private final String message;

    public MessageModel(String username, String photo, String message) {
        this.username = username;
        this.photo = photo;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoto() {
        return photo;
    }

    public String getMessage() {
        return message;
    }
}
