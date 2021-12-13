package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.IsSpamAPI;
import com.tezodevi.android.model.MessageAPI;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpamAPI {
    @POST("predict")
    Single<IsSpamAPI> isSpam(@Body MessageAPI message);
}
