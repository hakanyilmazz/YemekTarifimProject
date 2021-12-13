package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.ResponseRecipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ScoreAPI {
    @GET("score/competition")
    Observable<List<ResponseRecipe>> getRecipeListForCompetition(@Header("Authorization") String authHeader);
}
