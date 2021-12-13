package com.tezodevi.android.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;
import com.tezodevi.android.R;

public class ViewHelper {
    public static final int SPLASH_SCREEN_TOTAL_ANIMATION_TIME = 2;

    /**
     * Kullanıcı adı veya şifre yanlışsa hata mesajı gösterir
     */
    public static void showErrorForLogin(ViewBinding binding) {
        Snackbar.make(
                binding.getRoot(),
                binding.getRoot().getContext().getString(R.string.alert_text_wrong_login_input),
                Snackbar.LENGTH_SHORT
        ).show();
    }

    /**
     * Yeni bir ekrana geçişi sağlar
     *
     * @param thisActivity       bulunulan ekran
     * @param otherActivityClass gidilecek ekran
     * @param finish             bulunulan ekran arkaplandan kaldırılsın mı?
     */
    public static void createIntent(Activity thisActivity, Class<?> otherActivityClass, boolean finish) {
        Intent intentToOtherActivity = new Intent(thisActivity, otherActivityClass);

        if (finish) {
            thisActivity.finish();
        }

        thisActivity.startActivity(intentToOtherActivity);
    }

    public static void createIntentClear(Activity thisActivity, Class<?> otherActivityClass) {
        Intent intentToOtherActivity = new Intent(thisActivity, otherActivityClass);
        intentToOtherActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        thisActivity.startActivity(intentToOtherActivity);
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}
