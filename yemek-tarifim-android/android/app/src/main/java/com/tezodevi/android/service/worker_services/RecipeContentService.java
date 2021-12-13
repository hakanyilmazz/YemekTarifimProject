package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.model.RecipeContent;
import com.tezodevi.android.model.ResponseRecipeContent;
import com.tezodevi.android.repository.RecipeContentRepository;

import java.util.List;

import io.reactivex.Single;

public class RecipeContentService {
    private final RecipeContentRepository recipeContentRepository;

    public RecipeContentService(Context context) {
        recipeContentRepository = new RecipeContentRepository(context);
    }

    public Single<Boolean> postRecipeContent(List<RecipeContent> recipeContentList) {
        if (recipeContentList == null || recipeContentList.size() == 0) {
            return null;
        } else {
            for (RecipeContent recipeContent : recipeContentList) {
                if (recipeContent.recipeContentName.trim().isEmpty()) {
                    return null;
                }
            }
        }

        return recipeContentRepository.postRecipeContent(recipeContentList);
    }

    public Single<List<ResponseRecipeContent>> getRecipeContentsById(String recipeId) {
        return recipeContentRepository.getRecipeContentsById(recipeId);
    }
}
