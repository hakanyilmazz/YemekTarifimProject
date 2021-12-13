package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class FriendPostModel {
    @SerializedName("recipe")
    public ResponseRecipe recipe;

    @SerializedName("username")
    public String username;

}
