package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class MessageAPI {
    @SerializedName("message")
    public String message;

    public MessageAPI(String message) {
        this.message = message;
    }
}
