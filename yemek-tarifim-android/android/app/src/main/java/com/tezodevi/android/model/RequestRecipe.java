package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class RequestRecipe {
    @SerializedName("id")
    public String id;

    @SerializedName("recipePhoto")
    public byte[] recipePhoto;

    @SerializedName("recipeName")
    public String recipeName;

    @SerializedName("user")
    public RequestUser user;

    @SerializedName("recipeDetail")
    public String recipeDetail;

    public RequestRecipe(byte[] recipePhoto, String recipeName, String recipeDetail) {
        this.recipePhoto = recipePhoto;
        this.recipeName = recipeName;
        this.recipeDetail = recipeDetail;
    }

    public RequestRecipe(String id) {
        this.id = id;
    }
}
