package com.tezodevi.android.repository;

import android.content.Context;

import com.tezodevi.android.database.model.LifeChainAPI;
import com.tezodevi.android.database.model.LifeChainBlock;
import com.tezodevi.android.database.model.LifeChainRequest;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LifeChainRepository {
    private final String PATH = "api";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final LifeChainAPI lifeChainAPI;
    private final LoginService loginService;

    public LifeChainRepository(Context context) {
        this.lifeChainAPI = retrofit.create(LifeChainAPI.class);
        this.loginService = new LoginService(context);
    }

    public Observable<List<LifeChainBlock>> postLifeChain(LifeChainRequest lifeChainRequest) {
        return lifeChainAPI.postLifeChain(loginService.getToken(), loginService.getUsername(), lifeChainRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<LifeChainBlock>> getLifeChainList() {
        return lifeChainAPI.getLifeChainList(loginService.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
