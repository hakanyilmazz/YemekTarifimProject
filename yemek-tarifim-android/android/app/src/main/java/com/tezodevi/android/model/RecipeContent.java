package com.tezodevi.android.model;

import com.google.gson.annotations.SerializedName;

public class RecipeContent {
    @SerializedName("recipeContentName")
    public String recipeContentName;

    @SerializedName("recipe")
    public RequestRecipe recipe;

    public RecipeContent(String recipeContentName, RequestRecipe recipe) {
        this.recipeContentName = recipeContentName;
        this.recipe = recipe;
    }
}
