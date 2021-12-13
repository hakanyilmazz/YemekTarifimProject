package com.tezodevi.android.repository;

import android.content.Context;

import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.ScoreAPI;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ScoreRepository {
    private final String PATH = "api";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final ScoreAPI scoreAPI;
    private final LoginService loginService;

    public ScoreRepository(Context context) {
        this.scoreAPI = retrofit.create(ScoreAPI.class);
        loginService = new LoginService(context);
    }

    public Observable<List<ResponseRecipe>> getRecipeListForCompetition() {
        return scoreAPI.getRecipeListForCompetition(loginService.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
