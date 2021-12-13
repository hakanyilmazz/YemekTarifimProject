package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class LoginUser {
    @SerializedName("login")
    public Login login;

    @SerializedName("user")
    public RequestUser user;

    public LoginUser(Login login, RequestUser user) {
        this.login = login;
        this.user = user;
    }
}
