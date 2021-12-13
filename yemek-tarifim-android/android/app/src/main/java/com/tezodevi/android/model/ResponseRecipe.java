package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class ResponseRecipe {
    @SerializedName("id")
    public String id;

    @SerializedName("recipePhoto")
    public String recipePhoto;

    @SerializedName("recipeName")
    public String recipeName;

    @SerializedName("user")
    public ResponseUser user;

    @SerializedName("recipeDetail")
    public String recipeDetail;
}
