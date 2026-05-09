package com.tp.gestiondepenses.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private TextView tvProgressPercent;
    private static final long SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvProgressPercent = findViewById(R.id.tvProgressPercent);

        // Animation du pourcentage de 0 à 100% sur 2 secondes
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(SPLASH_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            tvProgressPercent.setText(String.format(Locale.getDefault(), "%d%%", progress));
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        animator.start();
    }
}