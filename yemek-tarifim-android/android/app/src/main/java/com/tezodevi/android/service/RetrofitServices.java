package com.tezodevi.android.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tezodevi.android.util.LocalhostHelper;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServices {
    private final String BASE_URL;
    private final RxJava2CallAdapterFactory rxJava2CallAdapterFactory;
    private final Retrofit retrofit;
    private final Gson gson;
    private final GsonConverterFactory gsonConverterFactory;

    /**
     * Retrofit nesnesi oluşturur.
     *
     * @param path base url dışındaki path, yani istek atılacak olan asıl önemli kısım.
     */
    public RetrofitServices(String path) {
        gson = new GsonBuilder().setLenient().create();
        this.BASE_URL = LocalhostHelper.getUrl(path);
        rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
        gsonConverterFactory = GsonConverterFactory.create(gson);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
