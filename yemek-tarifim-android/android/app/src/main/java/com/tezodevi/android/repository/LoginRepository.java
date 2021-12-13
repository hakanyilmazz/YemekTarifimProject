package com.tezodevi.android.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.tezodevi.android.model.EditRoleModel;
import com.tezodevi.android.model.JWT;
import com.tezodevi.android.model.Login;
import com.tezodevi.android.model.LoginUser;
import com.tezodevi.android.model.Role;
import com.tezodevi.android.service.RetrofitServices;
import com.tezodevi.android.service.web_services.LoginAPI;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginRepository {
    private static final String SHARED_PREFERENCES_KEY_USERNAME = "username";
    private static final String SHARED_PREFERENCES_KEY_PASSWORD = "password";
    private static final String SHARED_PREFERENCES_KEY_JWT_TOKEN = "jwtToken";

    private final String PATH = "auth";
    private final Retrofit retrofit = new RetrofitServices(PATH).getRetrofit();
    private final SharedPreferences sharedPreferences;
    private final LoginAPI loginAPI;

    public LoginRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.loginAPI = retrofit.create(LoginAPI.class);
    }

    /**
     * Kullanıcı bilgilerinin işlenebileceği bir sonuç döner.
     * IO -> işlenir.
     * mainThread -> Gözlemlenir.
     */
    public Single<JWT> login(String username, String password) {

        Login login = new Login(username, password);

        return loginAPI.login(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Kullanıcı giriş bilgilerini kaydeder.
     */
    public void saveLoginInfo(String username, String password) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_USERNAME, username).apply();
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_PASSWORD, password).apply();
    }

    /**
     * Daha önce login yapılmışsa true değilse false
     *
     * @return login durumu
     */
    public boolean checkLoginStatus() {
        return sharedPreferences.contains(SHARED_PREFERENCES_KEY_USERNAME) &&
                sharedPreferences.contains(SHARED_PREFERENCES_KEY_PASSWORD);
    }

    public Single<JWT> login() {
        String username = sharedPreferences.getString(SHARED_PREFERENCES_KEY_USERNAME, "");
        String password = sharedPreferences.getString(SHARED_PREFERENCES_KEY_PASSWORD, "");

        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        return login(username, password);
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_KEY_JWT_TOKEN, token).apply();
    }

    public Single<JWT> register(LoginUser loginUser) {
        return loginAPI.register(loginUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public String getToken() {
        return sharedPreferences.getString(SHARED_PREFERENCES_KEY_JWT_TOKEN, "");
    }

    public String getUsername() {
        return sharedPreferences.getString(SHARED_PREFERENCES_KEY_USERNAME, "");
    }

    public Single<Login> getMe() {
        return loginAPI.getMe("Bearer " + getToken(), getUsername())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<EditRoleModel> getMyRole(String username) {
        return loginAPI.getMyRole("Bearer " + getToken(), username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<EditRoleModel>> getRolesForAdmin() {
        return loginAPI.getRolesForAdmin("Bearer " + getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<EditRoleModel>> getRolesForModerator() {
        return loginAPI.getRolesForModerator("Bearer " + getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Login> updateLogin(String token, String username, Login login) {
        return loginAPI.updateLogin(token, username, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void signOut() {
        sharedPreferences.edit().clear().commit();
    }

    public Observable<List<Role>> getRoles() {
        return loginAPI.getRoles("Bearer " + getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Login> changeRole(Role role, String selectedUsername) {
        return loginAPI.changeRole("Bearer " + getToken(), selectedUsername, role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
