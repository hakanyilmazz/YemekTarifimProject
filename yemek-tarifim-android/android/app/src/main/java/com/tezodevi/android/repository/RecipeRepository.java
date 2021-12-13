package com.tezodevi.android.repository;

import android.content.Context;

import com.tezodevi.android.model.FriendPostModel;
import com.tezodevi.android.model.RequestRecipe;
import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.RecipeAPI;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RecipeRepository {
    private final String PATH = "api";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final RecipeAPI recipeAPI;
    private final LoginService loginService;

    public RecipeRepository(Context context) {
        this.recipeAPI = retrofit.create(RecipeAPI.class);
        loginService = new LoginService(context);
    }

    public Single<String> postRecipe(RequestRecipe recipe) {
        return recipeAPI.postRecipe(loginService.getToken(), recipe, loginService.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<FriendPostModel>> getFriendsRecipes() {
        return recipeAPI.getFriendsRecipes(loginService.getToken(), loginService.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ResponseRecipe>> getAllRecipes() {
        return recipeAPI.getAllRecipes(loginService.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ResponseRecipe>> getMyRecipes() {
        return recipeAPI.getMyRecipes(loginService.getToken(), loginService.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
