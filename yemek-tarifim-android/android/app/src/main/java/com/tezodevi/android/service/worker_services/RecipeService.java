package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.model.FriendPostModel;
import com.tezodevi.android.model.RequestRecipe;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.repository.RecipeRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(Context context) {
        recipeRepository = new RecipeRepository(context);
    }

    public Single<String> postRecipe(RequestRecipe recipe) {
        String recipeName = recipe.recipeName.trim();
        String recipeDetail = recipe.recipeDetail.trim();
        byte[] recipePhoto = recipe.recipePhoto;

        if (recipeName.isEmpty() || recipeDetail.isEmpty() || recipePhoto == null || recipePhoto.length == 0) {
            return null;
        }

        return recipeRepository.postRecipe(recipe);
    }

    public Observable<List<FriendPostModel>> getFriendsRecipes() {
        return recipeRepository.getFriendsRecipes();
    }

    public Observable<List<ResponseRecipe>> getAllRecipes() {
        return recipeRepository.getAllRecipes();
    }

    public Observable<List<ResponseRecipe>> getMyRecipes() {
        return recipeRepository.getMyRecipes();
    }
}
