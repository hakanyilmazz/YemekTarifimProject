package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.model.UserList;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {
    @GET("user")
    Observable<List<UserList>> getAll(@Header("Authorization") String authHeader);

    @GET("user/{username}/me")
    Single<ResponseUser> getMe(@Header("Authorization") String authHeader, @Path("username") String username);

    @PUT("user/{username}/change-photo")
    Single<ResponseUser> changePhoto(@Header("Authorization") String authHeader, @Path("username") String username, @Body RequestUser requestUser);

    @PUT("user/{username}/update")
    Single<ResponseUser> updateUser(@Header("Authorization") String authHeader, @Path("username") String username, @Body RequestUser requestUser);

    @PUT("user/{username}/add-friend")
    Single<ResponseUser> addFriend(@Header("Authorization") String authHeader, @Path("username") String username, @Body RequestUser friendUsername);

    @GET("user/{userId}/get-recipe-user")
    Single<String> getUsername(@Header("Authorization") String authHeader, @Path("userId") String userId);

    @PUT("user/{username}/remove-friend")
    Single<ResponseUser> removeFriend(@Header("Authorization") String authHeader, @Path("username") String username, @Body RequestUser friendUsername);

    @GET("user/{username}/friend-list")
    Single<List<UserList>> getFriendList(@Header("Authorization") String authHeader, @Path("username") String username);
}
