package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.FriendPostModel;
import com.tezodevi.android.model.RequestRecipe;
import com.tezodevi.android.model.ResponseRecipe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecipeAPI {
    @POST("recipe/{username}")
    Single<String> postRecipe(@Header("Authorization") String authHeader,
                              @Body RequestRecipe recipe,
                              @Path("username") String username);

    @GET("recipe/{username}/friend-recipe")
    Observable<List<FriendPostModel>> getFriendsRecipes(@Header("Authorization") String authHeader,
                                                        @Path("username") String username);

    @GET("recipe")
    Observable<List<ResponseRecipe>> getAllRecipes(@Header("Authorization") String authHeader);

    @GET("recipe/{username}/me")
    Observable<List<ResponseRecipe>> getMyRecipes(@Header("Authorization") String authHeader,
                                                  @Path("username") String username);
}
