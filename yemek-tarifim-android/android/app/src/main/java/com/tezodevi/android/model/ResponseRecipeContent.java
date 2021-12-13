package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class ResponseRecipeContent {
    @SerializedName("id")
    public String id;

    @SerializedName("recipe")
    public ResponseRecipe recipe;

    @SerializedName("recipeContentName")
    public String recipeContentName;
}
