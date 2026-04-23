package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class SplashActivity extends AppCompatActivity {

    // Durée du splash screen en millisecondes (2 secondes)
    private static final long SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Utilisation d'un Handler pour retarder le lancement de MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Redirection vers l'écran principal
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                // Fermer le splash ScreenActivity pour que l'utilisateur ne puisse pas y revenir
                finish();
                overridePendingTransition(0, 0); // Évite une double animation
            }
        }, SPLASH_DURATION);
    }
}