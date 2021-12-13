package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class Login {
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("email")
    public String email;

    @SerializedName("userRoleList")
    public Set<Role> userRoleList;

    public Login(String username, String password) {
        this(username, password, "");
    }

    public Login(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Login(String username) {
        this.username = username;
    }
}
