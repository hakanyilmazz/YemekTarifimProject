package com.tezodevi.android.service.web_services;

import com.tezodevi.android.model.EditRoleModel;
import com.tezodevi.android.model.JWT;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.LoginUser;
import com.tezodevi.android.model.Role;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// Backend ile iletişim kuracak olan api interface'i
public interface LoginAPI {

    @POST("login")
    Single<JWT> login(@Body Login login);

    @POST("register")
    Single<JWT> register(@Body LoginUser loginUser);

    @GET("detail/{username}/me")
    Single<Login> getMe(@Header("Authorization") String authHeader, @Path("username") String username);

    @GET("role/{username}/my-role")
    Single<EditRoleModel> getMyRole(@Header("Authorization") String authHeader, @Path("username") String username);

    @GET("role/admin")
    Observable<List<EditRoleModel>> getRolesForAdmin(@Header("Authorization") String authHeader);

    @GET("role/moderator")
    Observable<List<EditRoleModel>> getRolesForModerator(@Header("Authorization") String authHeader);

    @PUT("detail/{username}/update")
    Single<Login> updateLogin(@Header("Authorization") String authHeader, @Path("username") String username, @Body Login login);

    @GET("role/all")
    Observable<List<Role>> getRoles(@Header("Authorization") String authHeader);

    @PUT("role/{username}/change-role")
    Single<Login> changeRole(@Header("Authorization") String authHeader, @Path("username") String username, @Body Role role);
}
