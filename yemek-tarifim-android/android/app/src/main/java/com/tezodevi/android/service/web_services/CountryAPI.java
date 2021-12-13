package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.Country;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface CountryAPI {

    @GET("country")
    Flowable<List<Country>> getAll();
}
