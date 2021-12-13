package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class Role {
    @SerializedName("id")
    public String id;

    @SerializedName("roleName")
    public String roleName;

    public Role(String id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
