package com.tezodevi.android.service.worker_services;

import android.content.Context;

import com.tezodevi.android.model.EditRoleModel;
import com.tezodevi.android.model.JWT;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.LoginUser;
import com.tezodevi.android.model.Role;
import com.tezodevi.android.repository.LoginRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(Context context) {
        loginRepository = new LoginRepository(context);
    }

    /**
     * Login işlemini gerçekleştiren bir sonuç döndürür.
     * Not: Null dönebilir.
     *
     * @param username kullanıcı adı
     * @param password şifre
     * @return null ya da işlenebilir response
     */
    public Single<JWT> login(String username, String password) {
        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        return loginRepository.login(username, password);
    }

    /**
     * Not: Eğer daha önce login yapıldıysa kullanılmalı.
     * <p>
     * Login işlemini gerçekleştiren bir sonuç döndürür.
     *
     * @return null ya da işlenebilir response
     */
    public Single<JWT> login() {
        return loginRepository.login();
    }

    /**
     * Daha önce login yapıldıysa false, yapılmadıysa true.
     *
     * @return login statu
     */
    public boolean isLoginRequired() {
        return !loginRepository.checkLoginStatus();
    }

    /**
     * Kullanıcı giriş bilgilerini shared preferences'a kaydeder.
     */
    public void saveLoginInfo(String username, String password) {
        loginRepository.saveLoginInfo(username, password);
    }

    public void saveToken(String token) {
        loginRepository.saveToken(token);
    }

    public Single<JWT> register(LoginUser loginUser) {
        boolean validRegisterInputs = isValidRegisterInputs(loginUser);
        if (validRegisterInputs) {
            return loginRepository.register(loginUser);
        }

        return null;
    }

    private boolean isValidRegisterInputs(LoginUser loginUser) {
        String username = loginUser.login.username.trim();
        String password = loginUser.login.password.trim();
        String email = loginUser.login.email.trim();
        String name = loginUser.user.name.trim();
        String surname = loginUser.user.surname.trim();
        Integer age = loginUser.user.age;
        String countryId = loginUser.user.country.id;

        return !username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !name.isEmpty() &&
                !surname.isEmpty() && age > 0 && !countryId.equals("");
    }

    public String getToken() {
        String token = loginRepository.getToken();
        if (token.isEmpty()) {
            return "";
        }

        return "Bearer " + token;
    }

    public String getUsername() {
        return loginRepository.getUsername();
    }

    public Single<Login> getMe() {
        return loginRepository.getMe();
    }

    public Single<EditRoleModel> getMyRole() {
        return loginRepository.getMyRole(getUsername());
    }

    public Observable<List<Role>> getRoles() {
        return loginRepository.getRoles();
    }

    public Observable<List<EditRoleModel>> getRolesForAdmin() {
        return loginRepository.getRolesForAdmin();
    }

    public Observable<List<EditRoleModel>> getRolesForModerator() {
        return loginRepository.getRolesForModerator();
    }

    public Single<Login> updateLogin(Login login) {
        login.password = login.password.trim();
        login.email = login.email.trim();
        login.username = login.username.trim();

        if (login.password.length() < 6 || login.email.length() < 6 || login.username.length() < 6) {
            return null;
        }

        return loginRepository.updateLogin(getToken(), getUsername(), login);
    }

    public Single<JWT> getTokenFromDb(String username, String password) {
        return loginRepository.login(username, password);
    }

    public void signOut() {
        loginRepository.signOut();
    }

    public Single<Login> changeRole(Role role, String selectedUsername) {
        return loginRepository.changeRole(role, selectedUsername);
    }
}
