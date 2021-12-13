package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.RecipeContent;
import com.tezodevi.android.model.ResponseRecipeContent;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecipeContentAPI {
    @POST("recipe-content")
    Single<Boolean> postRecipeContent(@Header("Authorization") String authHeader,
                                      @Body List<RecipeContent> recipeContentList);

    @GET("recipe-content/{recipeId}/contents")
    Single<List<ResponseRecipeContent>> getRecipeContentsById(@Header("Authorization") String authHeader,
                                                              @Path("recipeId") String recipeId);
}
