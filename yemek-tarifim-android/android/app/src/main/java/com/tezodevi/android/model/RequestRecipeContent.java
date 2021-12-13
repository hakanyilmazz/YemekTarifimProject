package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class RequestRecipeContent {
    @SerializedName("id")
    public String id;

    @SerializedName("recipe")
    public RequestRecipe recipe;

    @SerializedName("recipeContentName")
    public String recipeContentName;

    public RequestRecipeContent(String recipeContentName) {
        this.recipeContentName = recipeContentName;
    }
}
