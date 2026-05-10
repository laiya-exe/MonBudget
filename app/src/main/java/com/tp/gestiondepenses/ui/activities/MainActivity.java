package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tp.gestiondepenses.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // IMPORTANT : corrige la top bar
        View topBar = findViewById(R.id.layout_top_bar);

        ViewCompat.setOnApplyWindowInsetsListener(topBar, (view, insets) -> {

            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;

            view.setPadding(
                    view.getPaddingLeft(),
                    statusBarHeight,
                    view.getPaddingRight(),
                    view.getPaddingBottom()
            );

            return insets;
        });

        // NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}