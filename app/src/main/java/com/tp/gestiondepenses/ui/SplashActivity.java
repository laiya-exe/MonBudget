package com.tp.gestiondepenses.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Attendre 2 secondes puis ouvrir RevenusActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, RevenusActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
