package com.tezodevi.android.repository;

import android.content.Context;

import com.tezodevi.android.model.RecipeContent;
import com.tezodevi.android.model.ResponseRecipeContent;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.RecipeContentAPI;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RecipeContentRepository {
    private final String PATH = "api";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final RecipeContentAPI recipeContentAPI;
    private final LoginService loginService;

    public RecipeContentRepository(Context context) {
        this.recipeContentAPI = retrofit.create(RecipeContentAPI.class);
        loginService = new LoginService(context);
    }

    public Single<Boolean> postRecipeContent(List<RecipeContent> recipeContentList) {
        return recipeContentAPI.postRecipeContent(loginService.getToken(), recipeContentList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<ResponseRecipeContent>> getRecipeContentsById(String recipeId) {
        return recipeContentAPI.getRecipeContentsById(loginService.getToken(), recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
