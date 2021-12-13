package com.tezodevi.android.service.worker_services;

import com.tezodevi.android.model.Country;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.CountryAPI;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

public class CountryService {
    private static final String COUNTRY_PATH = "api";
    private final Retrofit retrofit;

    public CountryService() {
        this.retrofit = new RetrofitServices(COUNTRY_PATH).getRetrofit();
    }

    public Flowable<List<Country>> getAll() {
        return retrofit.create(CountryAPI.class).getAll();
    }
}
