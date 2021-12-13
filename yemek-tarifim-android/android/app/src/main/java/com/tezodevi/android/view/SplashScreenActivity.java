package com.tezodevi.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;
import com.tezodevi.android.databinding.ActivitySplashScreenBinding;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.util.ViewHelper;
import com.tezodevi.android.view.auth.LoginActivity;
import com.tezodevi.android.view.user.UserActivity;

import io.reactivex.disposables.CompositeDisposable;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Animasyonun çalışacağı toplam süre: milisaniye cinsinden
     */
    private static final short ANIMATION_TOTAL_MS = ViewHelper.SPLASH_SCREEN_TOTAL_ANIMATION_TIME * 1000;

    /**
     * Animasyonun çalışacağı her saniye ne kadar olacak?: milisaniye cinsinden
     */
    private static final short ANIMATION_EACH_MS = 1000;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ActivitySplashScreenBinding binding;
    private CountDownTimer timer;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        loginService = new LoginService(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        timer = null;
        binding = null;
    }

    /**
     * Splash Screen ekranının çalışabilmesi için gerekli olan CountDownTimer nesnesini oluşturur.
     */
    private void createTimer() {
        timer = new CountDownTimer(SplashScreenActivity.ANIMATION_TOTAL_MS, SplashScreenActivity.ANIMATION_EACH_MS) {

            @Override
            public void onTick(long millisUntilFinished) {
                /*
                Her milisaniye boyunca çalışacak method.
                  Uygulamanın her milisaniye boyunca yapacağı bir iş olmadığı için içi boş bırakıldı.
                 */
            }

            @Override
            public void onFinish() {
                // Zaman dolduğunda uygulama başlatılır.
                startApplication();
            }
        };
    }

    /**
     * Timer'ı çalıştırır.
     * Eğer timer nesnesi null ise önce nesneyi oluşturur, sonra çalıştırır.
     */
    private void startTimer() {
        if (isTimerNull()) {
            createTimer();
        }

        timer.start();
    }

    /**
     * Timer sürekli arkaplanda çalıştığı için hatalara sebep olabilir.
     * Bu yüzden timer'ı durdurmak gerkelidir.
     * Eğer timer null ise durdurmaya gerek yoktur.
     */
    private void stopTimer() {
        if (isTimerNull()) {
            return;
        }

        timer.cancel();
    }

    /**
     * @return Timer null ise true değilse false
     */
    private boolean isTimerNull() {
        return timer == null;
    }

    /**
     * Network bağlantısının yapılıp yapılmadığını kontrol eder.
     *
     * @param context Bulunulan activity
     * @return internet bağlantısı durumu, bağlıysa true, değilse false.
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /**
     * Uygulama internete bağlı değilse bir snackbar gösterilir.
     * Bu uyarı mesajında TRY AGAIN butonuna tıklanılırsa uygulama tekrar başlatılır.
     * Uygulama internete bağlıysa, uygulama başlatılır.
     */
    private void startApplication() {
        if (shouldAppStart()) {
            if (!loginService.isLoginRequired()) {

                compositeDisposable.add(
                        loginService.login()
                                .subscribe(jwt -> {
                                            String token = jwt.jwt;
                                            loginService.saveToken(token);

                                            if (token.isEmpty()) {
                                                ViewHelper.createIntent(SplashScreenActivity.this,
                                                        LoginActivity.class, true);
                                            } else {
                                                ViewHelper.createIntent(SplashScreenActivity.this,
                                                        UserActivity.class, true);
                                            }
                                        }, throwable -> {
                                            Toast.makeText(getApplicationContext(), R.string.unaccesable_account, Toast.LENGTH_LONG).show();
                                            ViewHelper.createIntent(SplashScreenActivity.this,
                                                    LoginActivity.class, true);
                                        }
                                )
                );
            } else {
                ViewHelper.createIntent(this, LoginActivity.class, true);
            }
        } else {
            Snackbar.make(
                    binding.getRoot(),
                    getString(R.string.title_text_please_connect_to_internet),
                    Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.button_text_try_again, v -> {
                Intent intentSelf = getIntent();
                finish();
                startActivity(intentSelf);
            }).show();
        }
    }

    /**
     * @return Internet bağlantısının yapılıp yapılmadığı.
     */
    private boolean shouldAppStart() {
        return isNetworkAvailable(this);
    }
}