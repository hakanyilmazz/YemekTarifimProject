package com.tezodevi.android.repository;

import android.content.Context;

import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.UserAPI;
import com.tezodevi.android.service.worker_services.LoginService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class UserRepository {
    private final String PATH = "api";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final UserAPI userAPI;
    private final LoginService loginService;

    public UserRepository(Context context) {
        this.userAPI = retrofit.create(UserAPI.class);
        loginService = new LoginService(context);
    }

    public Single<ResponseUser> getMe() {
        return userAPI.getMe(loginService.getToken(), loginService.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseUser> changePhoto(RequestUser requestUser) {
        return userAPI.changePhoto(loginService.getToken(), loginService.getUsername(), requestUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseUser> updateUser(RequestUser requestUser) {
        return userAPI.updateUser(loginService.getToken(), loginService.getUsername(), requestUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<UserList>> getAll() {
        return userAPI.getAll(loginService.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseUser> addFriend(RequestUser friend) {
        return userAPI.addFriend(loginService.getToken(), loginService.getUsername(), friend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<String> getUsername(String userId) {
        return userAPI.getUsername(loginService.getToken(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ResponseUser> removeFriend(RequestUser requestUser) {
        return userAPI.removeFriend(loginService.getToken(), loginService.getUsername(), requestUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<UserList>> getFriendList() {
        return userAPI.getFriendList(loginService.getToken(), loginService.getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
