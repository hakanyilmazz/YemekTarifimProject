package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class EditRoleModel {
    @SerializedName("profilePhoto")
    public String profilePhoto;

    @SerializedName("username")
    public String username;

    @SerializedName("roles")
    public Set<Role> roles;
}
