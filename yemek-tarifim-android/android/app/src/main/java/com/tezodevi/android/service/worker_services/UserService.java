package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.model.RequestUser;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.model.UserList;
import com.tezodevi.android.repository.UserRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class UserService {
    private final UserRepository userRepository;

    public UserService(Context context) {
        userRepository = new UserRepository(context);
    }

    public Single<ResponseUser> getMe() {
        return userRepository.getMe();
    }

    public Single<List<UserList>> getFriendList() {
        return userRepository.getFriendList();
    }

    public Single<String> getUsername(String userId) {
        return userRepository.getUsername(userId);
    }

    public Single<ResponseUser> changePhoto(RequestUser requestUser) {
        return userRepository.changePhoto(requestUser);
    }

    public Single<ResponseUser> updateUser(RequestUser requestUser) {
        return userRepository.updateUser(requestUser);
    }

    public Observable<List<UserList>> getAll() {
        return userRepository.getAll();
    }

    public Single<ResponseUser> addFriend(RequestUser requestUser) {
        return userRepository.addFriend(requestUser);
    }

    public Single<ResponseUser> removeFriend(RequestUser requestUser) {
        return userRepository.removeFriend(requestUser);
    }
}
