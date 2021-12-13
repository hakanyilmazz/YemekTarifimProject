package com.tezodevi.android.util;

import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.model.ResponseRecipeContent;

import java.util.List;

public class SingletonRecipe {
    private static SingletonRecipe instance;

    public ResponseRecipe recipe;
    public List<ResponseRecipeContent> recipeContentList;

    private SingletonRecipe() {
    }

    public static SingletonRecipe getInstance() {
        if (instance == null) {
            instance = new SingletonRecipe();
        }

        return instance;
    }
}
