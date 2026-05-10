package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // On vérifie si l'utilisateur est déjà connecté
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                Intent intent;
                if (isLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        }, SPLASH_DURATION);
    }
}
