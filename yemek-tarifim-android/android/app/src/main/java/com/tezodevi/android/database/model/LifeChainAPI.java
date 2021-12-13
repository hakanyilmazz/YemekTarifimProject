package com.tezodevi.android.database.model;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LifeChainAPI {
    @POST("score/{username}/lifechain")
    Observable<List<LifeChainBlock>> postLifeChain(@Header("Authorization") String authHeader,
                                                   @Path("username") String username,
                                                   @Body LifeChainRequest lifeChainRequest);

    @GET("score/lifechain")
    Observable<List<LifeChainBlock>> getLifeChainList(@Header("Authorization") String authHeader);

}
