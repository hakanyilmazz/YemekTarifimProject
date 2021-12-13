package com.tezodevi.android.model;

import java.util.List;

public class RequestPostRecipe {
    private final RequestRecipe recipe;
    private final List<RecipeContent> recipeContent;

    public RequestPostRecipe(RequestRecipe recipe, List<RecipeContent> recipeContent) {
        this.recipe = recipe;
        this.recipeContent = recipeContent;
    }

    public RequestRecipe getRecipe() {
        return recipe;
    }

    public List<RecipeContent> getRecipeContent() {
        return recipeContent;
    }
}
