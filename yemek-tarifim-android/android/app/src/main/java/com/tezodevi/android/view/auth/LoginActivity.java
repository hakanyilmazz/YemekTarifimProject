package com.tezodevi.android.view.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.ActivityLoginBinding;
import com.tezodevi.android.model.JWT;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.util.ViewHelper;
import com.tezodevi.android.view.LicencesActivity;
import com.tezodevi.android.view.user.UserActivity;

import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class LoginActivity extends AppCompatActivity {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityLoginBinding binding;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginService = new LoginService(this);

        // Gece modunu devredışı bırakır.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setClickListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        binding = null;
        loginService = null;
    }

    /**
     * Ekrandaki tıklanabilir nesnelere tıklanabilme özelliklerini verir.
     */
    private void setClickListeners() {
        binding.loginActivityAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHelper.createIntent(LoginActivity.this, LicencesActivity.class, false);
            }
        });

        binding.loginActivityLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked(v);
            }
        });

        binding.loginActivityRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked(v);
            }
        });
    }

    /**
     * Kullanıcı bilgilerini kontrol edip login işlemini gerçekleştiren methoda yönlendirir.
     */
    private void loginButtonClicked(View view) {
        String username = Objects.requireNonNull(binding.loginActivityUsernameEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.loginActivityPasswordEditText.getText()).toString().trim();

        if (isUserInputsEmpty(username, password)) {
            Snackbar.make(view, getString(R.string.title_text_invalid_user_inputs), Snackbar.LENGTH_SHORT).show();
        } else if (!isUserInputLengthValid(username, password)) {
            binding.loginActivityUsernameEditText.setError(getString(R.string.text_required_character_length));
            binding.loginActivityPasswordEditText.setError(getString(R.string.text_required_character_length));
        } else {
            login(username, password);
        }
    }

    /**
     * Kayıt olma ekranına yönlendirir.
     */
    private void registerButtonClicked(View view) {
        ViewHelper.createIntent(this, RegisterActivity.class, false);
    }

    /**
     * Kullanıcı adı ve şifrenin boş olup olmadığını kontrol eder. Boş değillerse true değeri döner.
     */
    private boolean isUserInputsEmpty(String username, String password) {
        return TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
    }

    /**
     * Kullanıcı adı ve şifre uzunluğunu doğrular
     */
    private boolean isUserInputLengthValid(String username, String password) {
        return username.length() >= 6 && password.length() >= 6;
    }

    /**
     * Kullanıcı adı ve şifre bilgisi ile login işlemi gerçekleştirilir. Ardından user ekranlarına
     * yönlendirir.
     */
    private void login(String username, String password) {
        Single<JWT> login = loginService.login(username, password);

        if (login != null) {
            compositeDisposable.add(
                    login.subscribe(jwt -> {
                        String token = jwt.jwt;

                        if (token.isEmpty()) {
                            ViewHelper.showErrorForLogin(binding);
                        } else {
                            ViewHelper.createIntent(LoginActivity.this, UserActivity.class, true);
                            loginService.saveLoginInfo(username.trim(), password.trim());
                            loginService.saveToken(token);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        ViewHelper.showErrorForLogin(binding);
                    })
            );
        }
    }


}