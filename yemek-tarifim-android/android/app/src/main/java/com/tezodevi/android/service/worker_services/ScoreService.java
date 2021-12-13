package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.model.ResponseRecipe;
import com.tezodevi.android.repository.ScoreRepository;

import java.util.List;

import io.reactivex.Observable;

public class ScoreService {
    private final ScoreRepository scoreRepository;

    public ScoreService(Context context) {
        scoreRepository = new ScoreRepository(context);
    }

    public Observable<List<ResponseRecipe>> getRecipeListForCompetition() {
        return scoreRepository.getRecipeListForCompetition();
    }
}
